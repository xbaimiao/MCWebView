package com.xbaimiao.minecraft.web.netty.handler.impl

import com.xbaimiao.minecraft.web.netty.handler.Handler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.QueryStringDecoder

/**
 * @Author xbaimiao
 * @Date 2021/12/2 21:14
 */
class GetHandler(context: ChannelHandlerContext, request: FullHttpRequest) : Handler(context, request) {

    private fun getGetParamsFromChannel(): Map<String, Any> {
        val params: MutableMap<String, Any> = HashMap()
        if (request.method() === HttpMethod.GET) {
            val decoder = QueryStringDecoder(request.uri())
            val paramList = decoder.parameters()
            for ((key, value) in paramList) {
                params[key] = value[0]
            }
            return params
        }
        return params
    }

    val params = this.getGetParamsFromChannel()

}