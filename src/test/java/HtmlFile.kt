import com.xbaimiao.minecraft.web.netty.html.HtmlFile
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
        @JvmStatic
        fun main(args: Array<String>) {
            val file = HtmlFile("E:\\Intellij IDEA\\web\\ktor-sample\\src\\main\\resources\\templates\\index.ftl")
            file.eval(hashMapOf("d" to file))
        }

        val regex = Regex("\\$\\{[A-Za-z.\\u4e00-\\u9fa5]+}")
        val pattern = regex.toPattern()
    }

    var html: String

    init {
        if (!this.exists()) {
            throw NullPointerException("file not found")
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

    fun eval(map: HashMap<String, Any>): String {
        val list = arrayListOf<String>()
        val matcher = pattern.matcher(html)
        while (matcher.find()) {
            list.add(matcher.group())
        }
        println(list.toString())
        return ""
    }

}