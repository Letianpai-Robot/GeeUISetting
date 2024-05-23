package com.robot.geeui.setting

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors

open class BaseActivity : AppCompatActivity() {
     val singleExecutor = Executors.newSingleThreadExecutor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTitleAndNavigationBar()
    }

    private fun hideTitleAndNavigationBar() {
        val decorView = window.decorView
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }
}