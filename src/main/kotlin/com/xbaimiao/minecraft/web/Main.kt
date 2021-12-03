//package com.xbaimiao.minecraft.web
//
//import com.xbaimiao.minecraft.web.netty.NettyHttpServer
//import com.xbaimiao.minecraft.web.netty.handler.ImageType
//import taboolib.common.io.newFile
//import taboolib.common.platform.Plugin
//import taboolib.module.configuration.Config
//import taboolib.module.configuration.SecuredFile
//import taboolib.platform.BukkitPlugin
//import java.io.File
//import java.net.URL
//import javax.imageio.ImageIO
//
//object Main : Plugin() {
//
//    @Config(value = "config.yml", migrate = true)
//    lateinit var conf: SecuredFile
//        private set
//
//    lateinit var server: NettyHttpServer
//    lateinit var thread: Thread
//
//    val plugin by lazy { BukkitPlugin.getInstance() }
//
//    override fun onEnable() {
//        thread = Thread {
//            server = NettyHttpServer(conf.getInt("port")) {
//                installHtml("${plugin.dataFolder}${File.separator}")
//                installStatic(plugin.dataFolder.path)
//                get("/test") {
//                    println(this.getGetParamsFromChannel().toString())
//                    respondImage(ImageIO.read(URL("http://127.0.0.1:8080/favicon.png")))
//                }
//                get("/index") {
//                    respondHtml(html("index.html"), mapOf("config" to com.xbaimiao.minecraft.web.util.Config))
//                }
//                get("/favicon.png") {
//                    respondImage(newFile(plugin.dataFolder, "favicon.png"), ImageType.PNG)
//                }
//                post("/login") {
//                    println(this.getPostParamsFromChannel().toString())
//                }
//            }
//        }
//        thread.start()
//    }
//
//    override fun onDisable() {
//        thread.stop()
//    }
//
//}