package com.wahyouwebid.recentapp.lib.navbar

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.wahyouwebid.recentapp.lib.navbar.OSUtils.isEMUI
import com.wahyouwebid.recentapp.lib.navbar.OSUtils.isEMUI3_x
import com.wahyouwebid.recentapp.lib.navbar.OSUtils.isMIUI

/**
 * copy from https://github.com/gyf-dev/ImmersionBar.
 * 导航栏显示隐藏处理，目前只支持emui和miui带有导航栏的手机
 *
 * @author geyifeng
 * @date 2019/4/10 6:02 PM
 */
class NavigationBarObserver: ContentObserver(
    Handler(Looper.getMainLooper())
) {
    private var mListeners: ArrayList<OnNavigationBarListener>? = null
    private var context: Context? = null
    private var mIsRegister = false
    var activityContext: Context? = null


    fun register(context: Context) {
        activityContext = context
        this.context = context.applicationContext

        if (!mIsRegister) {
            val uri: Uri? = when {
                isMIUI -> Settings.Global.getUriFor(IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW)
                isEMUI -> if (isEMUI3_x) {
                    Settings.System.getUriFor(IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW)
                } else {
                    Settings.Global.getUriFor(IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW)
                }
                else -> null
            }

            uri?.let {
                context.contentResolver.registerContentObserver(it, true, this)
                mIsRegister = true
            }
        }
    }

    fun unregister() {
        context?.contentResolver?.unregisterContentObserver(this)
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        if (mListeners != null && mListeners?.isNotEmpty() == true) {
            val enabled = isGestureEnabled(context)
            for (onNavigationBarListener in mListeners!!) {
                onNavigationBarListener.onNavigationBarChange(enabled)
            }
        }
    }

    fun addOnNavigationBarListener(listener: OnNavigationBarListener?) {
        if (listener == null) return
        if (mListeners == null) {
            mListeners = ArrayList()
        }
        if (mListeners?.contains(listener) == false) {
            mListeners?.add(listener)
        }
    }

    fun removeOnNavigationBarListener(listener: OnNavigationBarListener?) {
        if (mIsRegister) {
            context!!.contentResolver.unregisterContentObserver(this)
            mIsRegister = false
        }
        this.context = null
        if (listener == null || mListeners == null) return
        mListeners?.remove(listener)
    }

    companion object {
        /**
         * MIUI ROM
         */
        const val IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW: String = "force_fsg_nav_bar"

        /**
         * EMUI ROM
         */
        const val IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW: String = "navigationbar_is_min"

        fun isAvailable(): Boolean = isMIUI || isEMUI

        fun isGestureEnabled(context: Context?): Boolean {
            val appContext = context?.applicationContext ?: return false
            val uriKey = when {
                isMIUI -> IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW
                isEMUI -> if (isEMUI3_x) IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW else IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW
                else -> return false
            }

            val show = if (isMIUI || (isEMUI && isEMUI3_x)) {
                Settings.System.getInt(appContext.contentResolver, uriKey, 0)
            } else {
                Settings.Global.getInt(appContext.contentResolver, uriKey, 0)
            }

            return show != 0
        }
    }
}