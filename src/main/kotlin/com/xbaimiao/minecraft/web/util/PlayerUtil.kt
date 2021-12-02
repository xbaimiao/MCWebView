package com.xbaimiao.minecraft.web.util

import com.google.gson.Gson
import org.bukkit.OfflinePlayer
import java.net.URL

object PlayerUtil {

    data class User(
        val id: String,
        val name: String
    )

    fun OfflinePlayer.getOnlineUUID(): String {
        val gson = Gson()
        println("正在获取 $name 的uuid")
        val data = URL("https://api.mojang.com/users/profiles/minecraft/$name").sendGet()
        if (data.contains("Not Found")) {
            return uniqueId.toString()
        }
        val user = gson.fromJson(data, User::class.java)
            ?: return uniqueId.toString()
        return user.id
    }

}