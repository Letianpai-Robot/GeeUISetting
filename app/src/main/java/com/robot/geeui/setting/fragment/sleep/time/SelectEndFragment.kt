package com.robot.geeui.setting.fragment.sleep.time

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.CommonBackActivity
import com.robot.geeui.setting.R
import com.robot.geeui.setting.fragment.BaseSecondFragment
import com.robot.geeui.setting.model.BaseModel
import com.robot.geeui.setting.model.SleepConfig
import com.robot.geeui.setting.widget.PickerView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class SelectEndFragment : BaseSecondFragment() {
    private var hourPicker: PickerView? = null
    private var minPicker: PickerView? = null
    var hour = "12"
    var min = "30"
    private var sleepConfig: SleepConfig? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_timeselect
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.findViewById<TextView>(R.id.title)
            ?.setText(resources.getString(R.string.time_endtime_desc))
        initView()
        var save = view?.findViewById<Button>(R.id.savebtn)
        save?.setOnClickListener {
            Log.i("TAG", "onViewCreated: ${hour}:${min} ")
            if (sleepConfig == null) {
                return@setOnClickListener
            }
            sleepConfig?.endTime = "$hour:$min"
            updateData()
        }
        getData()
    }

    private fun initView() {
        hourPicker = view?.findViewById<PickerView>(R.id.minute_pv)
        minPicker = view?.findViewById<PickerView>(R.id.second_pv)
        val data: MutableList<String> = ArrayList()
        val seconds: MutableList<String> = ArrayList()
        for (i in 0..23) {
            data.add(if (i < 10) "0$i" else "" + i)
        }
        for (i in 0..59) {
            seconds.add(if (i < 10) "0$i" else "" + i)
        }
        hourPicker?.setData(data)
        hourPicker?.setOnSelectListener { text ->
            hour = text
        }
        minPicker?.setData(seconds)
        minPicker?.setOnSelectListener { text ->
            min = text
        }
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

    private fun updateUI() {
        activity?.runOnUiThread {
            var time = sleepConfig?.endTime?.split(":")
            hour = time?.get(0) ?: "12"
            min = time?.get(1) ?: "30"
            activity?.runOnUiThread {
                hourPicker?.setSelected(Integer.parseInt(hour))
                minPicker?.setSelected(Integer.parseInt(min))
            }
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
                        Log.i("TAG", "onResponse: ${response.body.string()}")
                        activity?.runOnUiThread {
                            (activity as CommonBackActivity).switchPreFragment()
                        }
                    }
                })
        }
    }
}