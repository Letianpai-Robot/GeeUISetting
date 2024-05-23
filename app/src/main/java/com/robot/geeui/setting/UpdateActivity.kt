package com.robot.geeui.setting

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.databinding.ActivityUpdateBinding
import com.robot.geeui.setting.model.BaseModel
import com.robot.geeui.setting.model.OtaDataModel
import com.robot.geeui.setting.utils.SystemUtils
import com.robot.geeui.setting.utils.Util
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.Executors

class UpdateActivity : BaseActivity() {
    lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.included.back.setOnClickListener { finish() }
        binding.update.setOnClickListener {
            startOta()
        }
        singleExecutor.execute {
            getOTA()
        }
    }

    private fun startOta() {
        val intent = Intent()
        val cn = ComponentName(
            "com.letianpai.otaservice",
            "com.letianpai.otaservice.ota.GeeUpdateService"
        )
        intent.setComponent(cn)
        startService(intent)
    }

    private fun getOTA() {
        val localVersion: String = SystemUtils.getRomVersion()
        GeeUiNetManager.get(
            this,
            SystemUtil.isInChinese(),
            "your interface url",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    val type = object : TypeToken<BaseModel<OtaDataModel>>() {}.type
                    val data = Gson().fromJson<BaseModel<OtaDataModel>>(json, type)
                    Log.i("TAG", "onResponse: $data")
                    val romOtaVersion = data.baseData.romVersion
                    val status: Boolean = Util.compareVersion(
                        romOtaVersion,
                        localVersion
                    )
                    binding.data = data.baseData
                    binding.hasOta = status
                    Log.d(
                        "TAG",
                        "status:$status  otaRomVersion: $romOtaVersion---localVersion:$localVersion"
                    )
                }
            })
    }


}