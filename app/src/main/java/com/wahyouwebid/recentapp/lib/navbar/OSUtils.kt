package com.wahyouwebid.recentapp.lib.navbar

import android.annotation.SuppressLint
import android.text.TextUtils

object OSUtils {
    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_EMUI_VERSION_NAME = "ro.build.version.emui"
    private const val EMOTION_UI_V3 = "EmotionUI 3"
    private const val EMOTION_UI_V30 = "EmotionUI_3.0"
    private const val EMOTION_UI_V31 = "EmotionUI_3.1"

    @JvmStatic
    val isMIUI: Boolean
        /**
         * Is miui boolean.
         *
         * @return the boolean
         */
        get() {
            val property = getSystemProperty(KEY_MIUI_VERSION_NAME)
            return !TextUtils.isEmpty(property)
        }

    @JvmStatic
    val isEMUI: Boolean
        /**
         * Is emui boolean.
         *
         * @return the boolean
         */
        get() {
            val property = getSystemProperty(KEY_EMUI_VERSION_NAME)
            return !TextUtils.isEmpty(property)
        }

    private val eMUIVersion: String
        /**
         * Gets emui version.
         *
         * @return the emui version
         */
        get() = if (isEMUI) getSystemProperty(KEY_EMUI_VERSION_NAME) else ""

    private val isEMUI3_1: Boolean
        /**
         * Is emui 3 1 boolean.
         *
         * @return the boolean
         */
        get() {
            val property = eMUIVersion
            return EMOTION_UI_V3 == property || property.contains(EMOTION_UI_V31)
        }

    private val isEMUI3_0: Boolean
        /**
         * Is emui 3 1 boolean.
         *
         * @return the boolean
         */
        get() {
            val property = eMUIVersion
            return property.contains(EMOTION_UI_V30)
        }

    @JvmStatic
    val isEMUI3_x: Boolean
        /**
         * Is emui 3 x boolean.
         *
         * @return the boolean
         */
        get() = isEMUI3_0 || isEMUI3_1

    private fun getSystemProperty(key: String): String {
        try {
            @SuppressLint("PrivateApi") val clz = Class.forName("android.os.SystemProperties")
            val method = clz.getMethod("get", String::class.java, String::class.java)
            return method.invoke(clz, key, "") as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}
