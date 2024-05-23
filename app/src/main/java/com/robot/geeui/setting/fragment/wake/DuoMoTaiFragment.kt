package com.robot.geeui.setting.fragment.wake

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import com.google.gson.reflect.TypeToken
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.R
import com.robot.geeui.setting.fragment.BaseSecondFragment
import com.robot.geeui.setting.model.BaseModel
import com.robot.geeui.setting.model.WakeConfig
import com.robot.geeui.setting.model.WakeConfigChinese
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException

class DuoMoTaiFragment : BaseSecondFragment() {
    private var wakeData: WakeConfigChinese? = null
    private var switchbtn: Switch? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_duomotai
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchbtn = view?.findViewById<Switch>(R.id.switchbtn)

        getData()
    }

    private fun changeMoreModalSwitch(checked: Boolean) {
        var bodyMap = mutableMapOf(
            Pair("boy_child_voice_switch", wakeData?.boyChildVoiceSwitch),
            Pair("boy_voice_switch", wakeData?.boyVoiceSwitch),
            Pair("device_id", "setting"),
            Pair("girl_child_voice_switch", wakeData?.girlChildVoiceSwitch),
            Pair("girl_voice_switch", wakeData?.girlVoiceSwitch),
            Pair("letianpai_switch", wakeData?.letianpaiSwitch),
            Pair("more_modal_switch", if (checked) 1 else 0),
            Pair("robot_voice_switch", wakeData?.robotVoiceSwitch),
            Pair("xiaole_switch", wakeData?.xiaoleSwitch),
            Pair("xiaopai_switch", wakeData?.xiaopaiSwitch),
            Pair("xiaotian_switch", wakeData?.xiaotianSwitch),
        )
        singleExecutor.execute {
            GeeUiNetManager.post(
                context,
                SystemUtil.isInChinese(),
                "your interface url",
                bodyMap.toMutableMap() as HashMap,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.i("TAG", "onResponse: ${response.body.string()}")
                    }
                })
        }
    }

    private fun getData() {
        singleExecutor.execute {
            GeeUiNetManager.get(
                context,
                SystemUtil.isInChinese(),
                "your interface url",
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.i("TAG", "onFailure: ")
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var data = response.body.string();
                        var type = object : TypeToken<BaseModel<WakeConfigChinese>>() {}.type
                        var wakeConfig = gson.fromJson<BaseModel<WakeConfigChinese>>(data, type)
                        wakeData = wakeConfig.baseData
                        Log.i("TAG", "onResponse===: ${wakeConfig.toString()}")
                        wakeConfig.baseData.moreModalSwitch?.let { updateUI(it) }
                    }
                })
        }
    }

    private fun updateUI(moreModalSwitch: Int) {
        activity?.runOnUiThread {
            switchbtn?.isChecked = moreModalSwitch == 1
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
                changeMoreModalSwitch(isChecked)
            }
        }
    }

}