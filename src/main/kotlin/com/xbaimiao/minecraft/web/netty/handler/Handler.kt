package com.xbaimiao.minecraft.web.netty.handler

import com.xbaimiao.minecraft.web.netty.html.HtmlFile
import com.xbaimiao.minecraft.web.util.toByteArray
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.*
import java.awt.image.BufferedImage
import java.io.File

/**
 * @Author xbaimiao
 * @Date 2021/12/2 18:32
 */
open class Handler(val context: ChannelHandlerContext, val request: FullHttpRequest) {

    private fun respondFile(file: File, string: String) {
        val response = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK, Unpooled.wrappedBuffer(file.toByteArray())
        )
        response.headers()["Content-Type"] = "$string;charset=UTF-8"
        response.headers()["Content-Length"] = response.content().readableBytes()
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    fun respondFile(file: File) {
        respondFile(file, "application/x-msdownload")
    }

    fun respondMp4(file: File) {
        respondFile(file, "video/mpeg4")
    }

    fun respondMp3(file: File) {
        respondFile(file, "audio/mp3")
    }

    fun respondCss(file: File) {
        respondFile(file, "text/css")
    }

    fun respondJs(file: File) {
        respondFile(file, "application/x-javascript")
    }

    fun respondImage(image: BufferedImage) {
        val response = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK, Unpooled.wrappedBuffer(image.toByteArray())
        )
        response.headers()["Content-Type"] = "image/png;charset=UTF-8"
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