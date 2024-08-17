package com.wahyouwebid.recentapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.wahyouwebid.recentapp.lib.RecentAppsThumbnailHidingLifecycleTracker

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(RecentAppsThumbnailHidingLifecycleTracker())
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
