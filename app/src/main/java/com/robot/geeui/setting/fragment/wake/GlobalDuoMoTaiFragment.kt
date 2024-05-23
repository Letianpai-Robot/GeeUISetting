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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException

class GlobalDuoMoTaiFragment : BaseSecondFragment() {
    private var wakeData: WakeConfig? = null
    private var switchbtn: Switch? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_duomotai
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchbtn = view?.findViewById<Switch>(R.id.switchbtn)
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
        getData()
    }

    private fun changeMoreModalSwitch(checked: Boolean) {
        var bodyMap = mutableMapOf(
            Pair("boy_voice_switch", wakeData?.boyVoiceSwitch),
            Pair("client_id", "setting"),
            Pair("create_time", wakeData?.createTime),
            Pair("custom_pinyin", wakeData?.customPinyin),
            Pair("custom_switch", wakeData?.customSwitch),
            Pair("custom_title", wakeData?.customTitle),
            Pair("girl_voice_switch", wakeData?.girlVoiceSwitch),
            Pair("op_user_id", wakeData?.opUserId),
            Pair("rux_switch", wakeData?.ruxSwitch),
            Pair("more_modal_switch", if (checked) 1 else 0),
        )
        singleExecutor.execute {
            GeeUiNetManager.post(
                context,
                false,
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
                        var type = object : TypeToken<BaseModel<WakeConfig>>() {}.type
                        var wakeConfig = gson.fromJson<BaseModel<WakeConfig>>(data, type)
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
        }
    }

}