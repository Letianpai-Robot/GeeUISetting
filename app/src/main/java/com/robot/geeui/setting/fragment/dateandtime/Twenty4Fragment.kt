package com.robot.geeui.setting.fragment.dateandtime

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
import com.robot.geeui.setting.model.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException

class Twenty4Fragment : BaseSecondFragment() {
    private var data: DateAndTime? = null
    private var switchbtn: Switch? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_duomotai
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchbtn = view?.findViewById<Switch>(R.id.switchbtn)
        view?.findViewById<TextView>(R.id.wifi)
            ?.setText(getString(R.string.dateandtime_24_desc) + "?")
        getData()
    }

    private fun updateData() {
        var bodyMap = mutableMapOf(
            Pair("device_id", "setting"),
            Pair("date_format", data?.dateFormat),
            Pair("hour_24_switch", data?.hour24Switch),
            Pair("time_zone", data?.timeZone),
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
                        var json = response.body.string();
                        var type = object : TypeToken<BaseModel<DateAndTime>>() {}.type
                        var wakeConfig = gson.fromJson<BaseModel<DateAndTime>>(json, type)
                        data = wakeConfig.baseData
                        Log.i("TAG", "onResponse===: ${wakeConfig.toString()}")
                        updateUI()
                    }
                })
        }
    }

    private fun updateUI() {
        activity?.runOnUiThread {
            if (data == null) return@runOnUiThread
            switchbtn?.isChecked = data?.hour24Switch == 1
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
                data?.hour24Switch = if (isChecked) 1 else 0
                updateData()
            }
        }
    }


}