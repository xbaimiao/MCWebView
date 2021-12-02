package com.xbaimiao.minecraft.web.netty

import com.xbaimiao.minecraft.web.netty.handler.impl.GetHandler
import com.xbaimiao.minecraft.web.netty.handler.Handler
import com.xbaimiao.minecraft.web.netty.handler.impl.PostHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpMethod
import java.io.File

class NettyHttpServerHandler(private val server: NettyHttpServer) : SimpleChannelInboundHandler<FullHttpRequest>() {

    override fun channelRead0(channelHandlerContext: ChannelHandlerContext, fullHttpRequest: FullHttpRequest) {
        val uri = fullHttpRequest.uri() ?: return
        if (fullHttpRequest.method() == HttpMethod.GET) {
            for (mutableEntry in server.getMap) {
                if (uri.startsWith(mutableEntry.key)) {
                    mutableEntry.value.invoke(GetHandler(channelHandlerContext, fullHttpRequest))
                    return
                }
            }
        } else {
            for (mutableEntry in server.postMap) {
                if (uri.startsWith(mutableEntry.key)) {
                    mutableEntry.value.invoke(PostHandler(channelHandlerContext, fullHttpRequest))
                    return
                }
            }
        }
        Handler(
            channelHandlerContext,
            fullHttpRequest
        ).respondFile(File("${server.static}${File.separator}${uri.substring(1)}"))
    }

}