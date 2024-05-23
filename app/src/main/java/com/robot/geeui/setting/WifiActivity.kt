package com.robot.geeui.setting

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView


class WifiActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi)
        findViewById<TextView>(R.id.wifi).text = "Wi-Fi:" + getSSID()
        findViewById<View>(R.id.back).setOnClickListener { finish() }
        findViewById<View>(R.id.change).setOnClickListener {
            openWifiConnectView()
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.wifi).text = "Wi-Fi:" + getSSID()
    }

    /**
     * 获取当前连接WIFI的SSID
     */
    fun getSSID(): String? {
        val wm: WifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wm != null) {
            val winfo: WifiInfo = wm.getConnectionInfo()
            wm.connectionInfo
            if (winfo != null) {
                val s: String = winfo.getSSID()
                Log.i("TAG", "getSSID: " + s)
                return if (s.length > 2 && s[0] == '"' && s[s.length - 1] == '"') {
                    s.substring(1, s.length - 1)
                } else {
                    s
                }
            }
        }
        return ""
    }

    private fun openWifiConnectView() {
        val packageName = "com.letianpai.robot.wificonnet"
        val activityName = "com.letianpai.robot.wificonnet.MainActivity"
        val intent = Intent()
        intent.component = ComponentName(packageName, activityName)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("from", "from_title")
        startActivity(intent)
    }
    
}