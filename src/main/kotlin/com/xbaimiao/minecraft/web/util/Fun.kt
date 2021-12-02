package com.xbaimiao.minecraft.web.util

import java.io.*
import java.net.*

/**
 * 将文件转换成byte数组
 */
fun File.toByteArray():ByteArray{
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

fun main() {
    val data = URL("https://api.mojang.com/users/profiles/minecraft/xbaimiao").sendGet()
    println(data)
}

fun URL.encodeUrl(): URL {
    val u = this.punyUrl()
    return try {
        var urlS = u.toExternalForm()
        urlS = urlS.replace(" ", "%20")
        val uri = URI(urlS)
        URL(uri.toASCIIString())
    } catch (e: URISyntaxException) {
        u
    } catch (e: MalformedURLException) {
        u
    }
}

fun URL.punyUrl(): URL {
    var url = this
    if (!url.host.isAscii()) {
        url = try {
            val puny = IDN.toASCII(url.host)
            URL(url.protocol, puny, url.port, url.file)
        } catch (e: MalformedURLException) {
            throw IllegalArgumentException(e)
        }
    }
    return url
}

fun String.isAscii(): Boolean {
    for (i in 0 until this.length) {
        val c = this[i].code
        if (c > 127) { // ascii range
            return false
        }
    }
    return true
}