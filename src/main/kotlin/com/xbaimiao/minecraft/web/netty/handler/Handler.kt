package com.xbaimiao.minecraft.web.netty.handler

import com.xbaimiao.minecraft.web.netty.html.HtmlFile
import com.xbaimiao.minecraft.web.util.toByteArray
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.*
import java.io.File

/**
 * @Author xbaimiao
 * @Date 2021/12/2 18:32
 */
open class Handler(val context: ChannelHandlerContext, val request: FullHttpRequest) {

    fun respondFile(file: File) {
        val response = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK, Unpooled.wrappedBuffer(file.toByteArray())
        )
        response.headers()["Content-Type"] = "application/x-msdownload;charset=UTF-8"
        response.headers()["Content-Length"] = response.content().readableBytes()
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    fun respondHtml(htmlFile: HtmlFile, map: Map<String, Any>? = null) {
        val html = if (map == null) {
            htmlFile.html
        } else {
            htmlFile.eval(map)
        }
        val response = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK, Unpooled.wrappedBuffer(html.toByteArray())
        )
        response.headers()["Content-Type"] = "text/html;charset=UTF-8"
        response.headers()["Content-Length"] = response.content().readableBytes()
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    fun respondImage(file: File, imageType: ImageType) {
        val response = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK, Unpooled.wrappedBuffer(file.toByteArray())
        )
        response.headers()["Content-Type"] = "${imageType.content};charset=UTF-8"
        response.headers()["Content-Length"] = response.content().readableBytes()
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    fun responseText(string: String) {
        val response: FullHttpResponse = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK, Unpooled.wrappedBuffer(string.toByteArray())
        )
        response.headers()["Content-Type"] = "text/plain;charset=UTF-8"
        response.headers()["Content-Length"] = response.content().readableBytes()
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

}