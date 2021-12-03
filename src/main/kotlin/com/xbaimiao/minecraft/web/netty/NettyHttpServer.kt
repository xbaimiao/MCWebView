package com.xbaimiao.minecraft.web.netty

import com.xbaimiao.minecraft.web.netty.handler.impl.GetHandler
import com.xbaimiao.minecraft.web.netty.handler.impl.PostHandler
import com.xbaimiao.minecraft.web.netty.html.HtmlFile
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption.*
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder

class NettyHttpServer(inetPort: Int, block: NettyHttpServer.() -> Unit = {}) {

    private val parentGroup: EventLoopGroup
    private val childGroup: EventLoopGroup
    private val server: ServerBootstrap
    private val future: ChannelFuture
    private var htmlPrefix = ""
    var static = ""
    val getMap = HashMap<String, GetHandler.() -> Unit>()
    val postMap = HashMap<String, PostHandler.() -> Unit>()

    fun get(path: String, block: GetHandler.() -> Unit) = let { getMap[path] = block }

    fun post(path: String, block: PostHandler.() -> Unit) = let { postMap[path] = block }

    fun installStatic(path: String) = let { static = path }

    fun installHtml(path: String) = let { htmlPrefix = path }

    fun html(filename: String): HtmlFile = HtmlFile(htmlPrefix + filename)

    fun handler(block: NettyHttpServer.() -> Unit) {
        block.invoke(this)
    }

    init {
        parentGroup = NioEventLoopGroup()
        childGroup = NioEventLoopGroup()
        try {
            server = ServerBootstrap()
            // 1. 绑定两个线程组分别用来处理客户端通道的accept和读写时间
            server.group(parentGroup, childGroup) // 2. 绑定服务端通道NioServerSocketChannel
                .channel(NioServerSocketChannel::class.java) // 3. 给读写事件的线程通道绑定handler去真正处理读写
                // ChannelInitializer初始化通道SocketChannel
                .childHandler(object : ChannelInitializer<SocketChannel?>() {
                    override fun initChannel(p0: SocketChannel?) {
                        p0 ?: return
                        // 请求解码器
                        p0.pipeline().addLast("http-decoder", HttpRequestDecoder())
                        // 将HTTP消息的多个部分合成一条完整的HTTP消息
                        p0.pipeline().addLast("http-aggregator", HttpObjectAggregator(65535))
                        // 响应转码器
                        p0.pipeline().addLast("http-encoder", HttpResponseEncoder())
                        // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
                        p0.pipeline().addLast("http-chunked", NettyHttpServerHandler(this@NettyHttpServer))
                        // 自定义处理handler
                        p0.pipeline().addLast("http-server", NettyHttpServerHandler(this@NettyHttpServer))
                    }
                })
            // 4. 监听端口（服务器host和port端口），同步返回
            // ChannelFuture future = server.bind(inetHost, this.inetPort).sync();
            server.option(WRITE_BUFFER_HIGH_WATER_MARK, 1048576 * 20)
            server.option(SO_KEEPALIVE, false)
            server.option(TCP_NODELAY, true)
            future = server.bind(inetPort).sync()
            block.invoke(this)
            // 当通道关闭时继续向后执行，这是一个阻塞方法
            future.channel().closeFuture().sync()
        } finally {
            childGroup.shutdownGracefully()
            parentGroup.shutdownGracefully()
        }
    }

}