package com.xbaimiao.minecraft.web.resources

import com.gmail.nossr50.datatypes.skills.PrimarySkillType
import com.gmail.nossr50.util.player.UserManager
import com.xbaimiao.minecraft.web.Main
import com.xbaimiao.minecraft.web.util.PlayerUtil.getOnlineUUID
import com.xbaimiao.minecraft.web.util.toByteArray
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import org.bukkit.OfflinePlayer
import taboolib.common.io.newFile
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

object Resources {

    fun File.sendResponse(channel: ChannelHandlerContext, string: String) {
        val response: FullHttpResponse = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK, Unpooled.wrappedBuffer(this.toByteArray())
        )
        response.headers()["Content-Type"] = "$string;charset=UTF-8"
        response.headers()["Content-Length"] = response.content().readableBytes()
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    fun OfflinePlayer.response(): FullHttpResponse {
        val mcMMOPlayer = UserManager.getOfflinePlayer(name)
        if (mcMMOPlayer == null) {
            val response = DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, Unpooled.wrappedBuffer("Player Not Found".toByteArray())
            )
            response.headers()["Content-Type"] = "text/html;charset=UTF-8"
            response.headers()["Content-Length"] = response.content().readableBytes()
            return response
        }
        val file = newFile(Main.plugin.dataFolder, "player.html")
        val string = StringBuilder()
        val fileInputStream = FileInputStream(file)
        val inputStreamReader = InputStreamReader(fileInputStream, Charsets.UTF_8)
        val read = BufferedReader(inputStreamReader)
        var next: String?
        while (read.readLine().also { next = it } != null) {
            string.append(next).append("\n")
        }
        fileInputStream.close()
        inputStreamReader.close()
        read.close()
        var html = string.toString()
        html = html.replace("%uuid%", getOnlineUUID())
        html = html.replace("%0%", mcMMOPlayer.getSkillLevel(PrimarySkillType.TAMING).toString())
        html = html.replace("%1%", mcMMOPlayer.getSkillLevel(PrimarySkillType.MINING).toString())
        html = html.replace("%2%", mcMMOPlayer.getSkillLevel(PrimarySkillType.WOODCUTTING).toString())
        html = html.replace("%3%", mcMMOPlayer.getSkillLevel(PrimarySkillType.REPAIR).toString())
        html = html.replace("%4%", mcMMOPlayer.getSkillLevel(PrimarySkillType.UNARMED).toString())
        html = html.replace("%5%", mcMMOPlayer.getSkillLevel(PrimarySkillType.HERBALISM).toString())
        html = html.replace("%6%", mcMMOPlayer.getSkillLevel(PrimarySkillType.EXCAVATION).toString())
        html = html.replace("%7%", mcMMOPlayer.getSkillLevel(PrimarySkillType.ARCHERY).toString())
        html = html.replace("%8%", mcMMOPlayer.getSkillLevel(PrimarySkillType.SWORDS).toString())
        html = html.replace("%9%", mcMMOPlayer.getSkillLevel(PrimarySkillType.AXES).toString())
        html = html.replace("%10%", mcMMOPlayer.getSkillLevel(PrimarySkillType.ACROBATICS).toString())
        html = html.replace("%11%", mcMMOPlayer.getSkillLevel(PrimarySkillType.FISHING).toString())
        html = html.replace("%12%", mcMMOPlayer.getSkillLevel(PrimarySkillType.ALCHEMY).toString())
        html = html.replace("%13%", mcMMOPlayer.powerLevel.toString())
        val response = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK, Unpooled.wrappedBuffer(html.toByteArray())
        )
        response.headers()["Content-Type"] = "text/html;charset=UTF-8"
        response.headers()["Content-Length"] = response.content().readableBytes()
        return response
    }

}