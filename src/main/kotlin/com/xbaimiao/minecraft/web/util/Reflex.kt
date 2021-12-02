package com.xbaimiao.minecraft.web.util

/**
 * @Author xbaimiao
 * @Date 2021/12/2 18:09
 */
object Reflex {

    fun Any.getProperty(path: String): Any {
        return try {
            this::class.java.getDeclaredField(path).get(this)
        } catch (e: Exception) {
            this::class.java.getDeclaredMethod("get${path.substring(0, 1).uppercase() + path.substring(1)}")
                .invoke(this)
        }
    }

    @JvmName("getProperty1")
    fun <T> Any.getProperty(path: String): T {
        return try {
            this::class.java.getDeclaredField(path).get(this) as T
        } catch (e: Exception) {
            this::class.java.getDeclaredMethod("get${path.substring(0, 1).uppercase() + path.substring(1)}")
                .invoke(this) as T
        }
    }

}