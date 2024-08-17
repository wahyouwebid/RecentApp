package com.wahyouwebid.recentapp

import android.app.Activity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.wahyouwebid.recentapp.lib.RecentAppsThumbnailHidingActivity
import com.wahyouwebid.recentapp.lib.RecentAppsThumbnailHidingListener
import com.wahyouwebid.recentapp.lib.isSecureFlagEnabled
import com.wahyouwebid.recentapp.ui.theme.RecentAppTheme

class MainActivity : RecentAppsThumbnailHidingActivity(), RecentAppsThumbnailHidingListener {

    override val enableSecureFlagOnLowApiDevices: Boolean = true

    override val enableSecureFlagOnCustomGestureNavigationDevices: Boolean = true

    private var showRecent by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecentAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showRecent) {
                        RecentApp(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }

    override fun onRecentAppsTriggered(
        activity: Activity,
        inRecentAppsMode: Boolean
    ) {
        if (!isSecureFlagEnabled()) {
            showRecent = inRecentAppsMode
        }
    }
}

@Composable
fun RecentApp(modifier: Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.Red))
}