package com.xbaimiao.minecraft.web.util

/**
 * @Author xbaimiao
 * @Date 2021/12/2 12:11
 */
object Config {

    val serverHost = "huangmc.top"
    val serverPort = 25565
    val online get() = 1
    val serverIp get() = let { if (serverPort == 25565) return@let serverHost else return@let "$serverHost:$serverPort" }

}