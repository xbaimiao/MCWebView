package com.xbaimiao.minecraft.web.netty

import com.xbaimiao.minecraft.web.netty.handler.Handler
import com.xbaimiao.minecraft.web.netty.handler.ImageType
import com.xbaimiao.minecraft.web.netty.handler.impl.GetHandler
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
            if (server.debug) {
                println("GET -> url:$uri")
            }
            for (mutableEntry in server.getMap) {
                if (uri.contains("?")) {
                    if (uri.split("?")[0] == mutableEntry.key) {
                        mutableEntry.value.invoke(GetHandler(channelHandlerContext, fullHttpRequest))
                        return
                    }
                } else {
                    if (uri == mutableEntry.key) {
                        mutableEntry.value.invoke(GetHandler(channelHandlerContext, fullHttpRequest))
                        return
                    }
                }
            }
        } else {
            if (server.debug) {
                println("POST -> url:$uri")
            }
            for (mutableEntry in server.postMap) {
                if (uri.contains("?")) {
                    if (uri.split("?")[0] == mutableEntry.key) {
                        mutableEntry.value.invoke(PostHandler(channelHandlerContext, fullHttpRequest))
                        return
                    }
                } else {
                    if (uri == mutableEntry.key) {
                        mutableEntry.value.invoke(PostHandler(channelHandlerContext, fullHttpRequest))
                        return
                    }
                }
            }
        }
        if (server.debug) {
            println("static res")
        }
        val handler = Handler(
            channelHandlerContext,
            fullHttpRequest
        )
        val filePath = "${server.static}${File.separator}${uri.substring(1)}".split("?")[0]
        val file = File(filePath)
        if (!file.exists()) {
            handler.responseText("${file.name} Not Fount")
            return
        }
        when (filePath.substring(filePath.length - 3).lowercase()) {
            "png" -> handler.respondImage(file, ImageType.PNG)
            "jpg" -> handler.respondImage(file, ImageType.JPG)
            "gif" -> handler.respondImage(file, ImageType.GIF)
            "css" -> handler.respondCss(file)
            ".js" -> handler.respondJs(file)
            "mp3" -> handler.respondMp3(file)
            "mp4" -> handler.respondMp4(file)
            "tml" -> handler.respondHtml(file)
            "svg" -> handler.respondFile(file, "text/xml")
        }
        handler.respondFile(file)
    }

}