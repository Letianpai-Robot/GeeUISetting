package com.robot.geeui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.databinding.ActivityResetBinding
import com.robot.geeui.setting.utils.SystemUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class ResettingActivity : BaseActivity() {
    lateinit var binding: ActivityResetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cancel.setOnClickListener { finish() }
        binding.included.back.setOnClickListener { finish() }
        binding.confirmUnbind.setOnClickListener {
//            Log.i("TAG", "onCreate: recory")
//            Thread {
//                SystemUtils.restoreRobot(this)
//            }.start()
//            //恢复出厂设置
//            val recovery = Intent("android.intent.action.MASTER_CLEAR")
//            recovery.setPackage("android")
//            sendBroadcast(recovery)
//            Log.i("TAG", "onCreate: recory1")
            restore()
        }

    }

    private fun restore() {
        singleExecutor.execute {
            val map = HashMap<String, String>()
            map.put("device_id", SystemUtil.getLtpSn())
            GeeUiNetManager.post(this,
                SystemUtil.isInChinese(),
                "your interface url",
                map,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {

                    }
                })
        }
    }
}