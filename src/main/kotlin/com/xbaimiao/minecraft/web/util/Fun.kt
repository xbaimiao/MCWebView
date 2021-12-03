package com.xbaimiao.minecraft.web.util

import java.awt.image.BufferedImage
import java.io.*
import java.net.URL
import javax.imageio.ImageIO

object Fun {
    fun BufferedImage.toByteArray(): ByteArray {
        val out = ByteArrayOutputStream()
        ImageIO.write(this, "png", out)
        return out.toByteArray()
    }

    /**
     * 将文件转换成byte数组
     */
    fun File.toByteArray(): ByteArray {
        val fis = FileInputStream(this)
        val bos = ByteArrayOutputStream()
        val b = ByteArray(1024)
        var n: Int
        while (fis.read(b).also { n = it } != -1) {
            bos.write(b, 0, n)
        }
        fis.close()
        bos.close()
        return bos.toByteArray()
    }

    fun URL.sendGet(): String {
        try {
            val result = StringBuilder()
            val conn = openConnection()// 打开和URL之间的连接
            conn.doInput = true
            conn.doInput = true
            conn.setRequestProperty("accept", "*/*") // 设置通用的请求属性
            conn.setRequestProperty("connection", "Keep-Alive")
            conn.setRequestProperty("charset", "utf-8")
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)")
            conn.connectTimeout = 4000
            conn.connect() // 建立实际的连接
            val input =
                BufferedReader(InputStreamReader(conn.getInputStream(), "UTF-8")) // 定义BufferedReader输入流来读取URL的响应
            var line: String?
            while (input.readLine().also { line = it } != null) {
                result.append(line)
            }
            input.close()
            return result.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}


