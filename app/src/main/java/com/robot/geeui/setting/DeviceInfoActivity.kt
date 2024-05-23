package com.robot.geeui.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.databinding.ActivityDeviceinfoBinding
import com.robot.geeui.setting.model.BaseModel
import com.robot.geeui.setting.model.DeviceInfo
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.Executors

class DeviceInfoActivity : BaseActivity() {

    lateinit var binding: ActivityDeviceinfoBinding

    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTitleAndNavigationBar()
        binding = ActivityDeviceinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.unbind.setOnClickListener {
            binding.contentList.visibility = View.GONE
            binding.confirmLayout.visibility = View.VISIBLE
        }
        binding.included.back.setOnClickListener { finish() }
        binding.cancel.setOnClickListener {
            binding.contentList.visibility = View.VISIBLE
            binding.confirmLayout.visibility = View.GONE
        }
        binding.confirmUnbind.setOnClickListener {
            unbind()
        }

        binding.name.text = resources.getString(R.string.strname, SystemUtil.getLtpSn().takeLast(5))
        binding.sn.text = resources.getString(R.string.sn, SystemUtil.getLtpSn())
        binding.mcu.text = resources.getString(R.string.strmcu, SystemUtil.getMcu())
        binding.mac.text = resources.getString(R.string.mac, SystemUtil.getWlanMacAddress())
        binding.ip.text = resources.getString(R.string.ip, SystemUtil.getIp(this))
        binding.blueMac.text = resources.getString(R.string.bluemac, SystemUtil.getWlanMacAddress())
    }

    override fun onResume() {
        super.onResume()
        singleExecutor.execute {
            loadInfo()
        }
    }

    private fun unbind() {
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
                        runOnUiThread {
                            unbindSuccess()
                        }
                    }
                })
        }
    }

    private fun unbindSuccess() {
        Log.i("TAG", "unbindSuccess: ")
    }

    private fun loadInfo() {
        GeeUiNetManager.get(this,
            SystemUtil.isInChinese(),
            "your interface url",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    Log.i("TAG", "onResponse: $json")
                    val type = object : TypeToken<BaseModel<DeviceInfo>>() {}.type
                    val baseModel = gson.fromJson<BaseModel<DeviceInfo>>(json, type)
                    Log.i("TAG", "onResponse: $baseModel")
                    if (baseModel.baseData != null) {
                        binding.deviceInfo = baseModel.baseData
                        Log.i("TAG", "onResponse: +${baseModel.baseData}")
                    }
                }
            })
    }

    private fun hideTitleAndNavigationBar() {
        val decorView = window.decorView
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }
}