package com.robot.geeui.setting.fragment.sleep.time

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.R
import com.robot.geeui.setting.fragment.BaseSecondFragment
import com.robot.geeui.setting.model.BaseModel
import com.robot.geeui.setting.model.SleepConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class AutoFragment : BaseSecondFragment() {
    private var sleepConfig: SleepConfig? = null
    private var switchbtn: Switch? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_duomotai
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tips = view?.findViewById<TextView>(R.id.wifi)
        tips?.setText(getString(R.string.sleep_timeswitch_tips))
        switchbtn = view?.findViewById<Switch>(R.id.switchbtn)
        getData()
    }

    private fun getData() {
        singleExecutor.execute {
            GeeUiNetManager.get(context,
                SystemUtil.isInChinese(),
                "your interface url",
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val json = response.body.string()
                        var type = object : TypeToken<BaseModel<SleepConfig>>() {}.type
                        val data = gson.fromJson<BaseModel<SleepConfig>>(json, type)
                        sleepConfig = data.baseData
                        updateUI()
                    }
                })
        }
    }

    private fun updateData() {
        if (sleepConfig == null) return
        var bodyMap = mutableMapOf(
            Pair("close_screen_mode_switch", sleepConfig?.closeScreenModeSwitch),
            Pair("device_id", SystemUtil.getLtpSn()),
            Pair("start_time", sleepConfig?.startTime),
            Pair("end_time", sleepConfig?.endTime),
            Pair("sleep_mode_switch", sleepConfig?.sleepModeSwitch),
            Pair("sleep_sound_mode_switch", sleepConfig?.sleepSoundModeSwitch),
            Pair("sleep_time_status_mode_switch", sleepConfig?.sleepTimeStatusModeSwitch),
        )
        singleExecutor.execute {
            GeeUiNetManager.post(context,
                SystemUtil.isInChinese(),
                "your interface url",
                bodyMap.toMutableMap() as HashMap,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                    }
                })
        }
    }

    private fun updateUI() {
        activity?.runOnUiThread {
            switchbtn?.isChecked = sleepConfig?.closeScreenModeSwitch == 1
            if (switchbtn?.isChecked == true) {
                switchbtn?.setSwitchTextAppearance(context, R.style.SwitchTheme)
            } else {
                switchbtn?.setSwitchTextAppearance(context, R.style.SwitchThemeOff)
            }
            switchbtn?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    switchbtn?.setSwitchTextAppearance(context, R.style.SwitchTheme)
                } else {
                    switchbtn?.setSwitchTextAppearance(context, R.style.SwitchThemeOff)
                }
                sleepConfig?.closeScreenModeSwitch = if (isChecked) 1 else 0
                updateData()
            }
        }
    }

}