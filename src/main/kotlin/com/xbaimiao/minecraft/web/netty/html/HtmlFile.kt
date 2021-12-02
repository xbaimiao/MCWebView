package com.xbaimiao.minecraft.web.netty.html

import com.xbaimiao.minecraft.web.util.Config
import com.xbaimiao.minecraft.web.util.Reflex.getProperty
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * @Author xbaimiao
 * @Date 2021/12/2 17:19
 */
class HtmlFile(path: String) : File(path) {

    companion object {
        private val regex = Regex("\\$\\{[A-Za-z.\\u4e00-\\u9fa5]+}")
        private val regex1 = Regex("[A-Za-z.\\u4e00-\\u9fa5]+")
        private val pattern = regex.toPattern()
        private val pattern1 = regex1.toPattern()
    }

    var html: String

    init {
        if (!this.exists()) {
            throw NullPointerException("${this.path} file not found")
        }
        html = html()
    }

    private fun html(): String {
        val string = StringBuilder()
        FileInputStream(this).use { fileInput ->
            InputStreamReader(fileInput, Charsets.UTF_8).use { inputStreamReader ->
                BufferedReader(inputStreamReader).use { read ->
                    var next: String?
                    while (read.readLine().also { next = it } != null) {
                        string.append(next).append("\n")
                    }
                }
            }
        }
        return string.toString()
    }

    fun updateForFile() {
        html = html()
    }

    fun eval(map: Map<String, Any>): String {
        val varList = hashMapOf<String, String>()
        val matcher = pattern.matcher(html)
        while (matcher.find()) {
            val matcher1 = pattern1.matcher(matcher.group())
            matcher1.find()
            varList[matcher.group()] = (matcher1.group())
        }
        var newHtml = html
        varList.forEach { (k, v) ->
            val args = v.split(".")
            val any = map[args[0]] ?: throw NullPointerException("$k parameter not passed in")
            newHtml = newHtml.replace(k, any.getProperty(args[1]).toString())
        }
        return newHtml
    }

}