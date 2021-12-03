package com.xbaimiao.minecraft.web.netty.handler.impl

import com.xbaimiao.minecraft.web.netty.handler.Handler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder
import io.netty.handler.codec.http.multipart.InterfaceHttpData
import io.netty.handler.codec.http.multipart.MemoryAttribute
import io.netty.util.internal.StringUtil

/**
 * @Author xbaimiao
 * @Date 2021/12/2 19:37
 */
class PostHandler(context: ChannelHandlerContext, request: FullHttpRequest) : Handler(context, request) {

    val params = getPostParamsFromChannel()

    private fun getPostParamsFromChannel(): Map<String, Any> {
        var params: Map<String, Any> = HashMap()
        if (request.method() === HttpMethod.POST) {
            // 处理post 请求
            val strContentType = request.headers()["Content-Type"].trim { it <= ' ' }
            if (StringUtil.isNullOrEmpty(strContentType)) {
                return params
            }
            if (strContentType.contains("x-www-form-urlencoded")) {
                params = getFormParams()
            } else {
                return params
            }
        }
        return params
    }

    private fun getFormParams(): Map<String, Any> {
        val params: MutableMap<String, Any> = HashMap()
        val decoder = HttpPostRequestDecoder(DefaultHttpDataFactory(false), request)
        val postData = decoder.bodyHttpDatas
        for (data in postData) {
            if (data.httpDataType == InterfaceHttpData.HttpDataType.Attribute) {
                val attribute = data as MemoryAttribute
                params[attribute.name] = attribute.value
            }
        }
        return params
    }

}