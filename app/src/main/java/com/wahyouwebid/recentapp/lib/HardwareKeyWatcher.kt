package com.wahyouwebid.recentapp.lib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build

/**
 * https://stackoverflow.com/questions/34471366/detect-touch-event-of-navigation-buttons-inside-a-service-having-window
 */
class HardwareKeyWatcher(val context: Context) {
    @Suppress("DEPRECATION") //Need fix deprecation system dialog
    private val mFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    private var mListener: OnHardwareKeysPressedListener? = null
    private var mReceiver: InnerReceiver? = null

    interface OnHardwareKeysPressedListener {
        fun onHomePressed()
        fun onRecentAppsPressed()
    }

    init {
        mFilter.priority = FILTER_PRIORITY
    }

    fun setOnHardwareKeysPressedListenerListener(listener: OnHardwareKeysPressedListener?) {
        mListener = listener
        mReceiver = InnerReceiver()
    }

    fun startWatch() {
        if (mReceiver != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(mReceiver, mFilter, Context.RECEIVER_NOT_EXPORTED)
            } else {
                context.registerReceiver(mReceiver, mFilter)
            }
        }
    }


    fun stopWatch() {
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver)
        }
    }

    internal inner class InnerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            @Suppress("DEPRECATION") //Fix deprecation system dialog
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS == action) {
                val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY) ?: ""
                if (mListener != null) {
                    when (reason) {
                        SYSTEM_DIALOG_REASON_HOME_KEY -> {
                            mListener?.onHomePressed()
                        }

                        SYSTEM_DIALOG_REASON_RECENT_APPS -> {
                            mListener?.onRecentAppsPressed()
                        }

                        SYSTEM_DIALOG_REASON_RECENT_APPS_XIAOMI -> {
                            mListener?.onRecentAppsPressed()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val SYSTEM_DIALOG_REASON_KEY: String = "reason"
        private const val SYSTEM_DIALOG_REASON_HOME_KEY: String = "homekey"
        private const val SYSTEM_DIALOG_REASON_RECENT_APPS: String = "recentapps"
        private const val SYSTEM_DIALOG_REASON_RECENT_APPS_XIAOMI: String = "fs_gesture"
        private const val FILTER_PRIORITY: Int = 1000

    }
}
