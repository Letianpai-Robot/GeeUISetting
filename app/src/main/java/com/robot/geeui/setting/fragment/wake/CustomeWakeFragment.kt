package com.robot.geeui.setting.fragment.wake

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.robot.geeui.setting.R
import com.robot.geeui.setting.fragment.BaseSecondFragment

class CustomeWakeFragment : BaseSecondFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_textview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.findViewById<TextView>(R.id.textview)?.apply {
            setTextColor(Color.parseColor("#9e9e9e"))
            text = "请在手机APP中的\n" +
                    "“自定义唤醒词”\n" +
                    "进行设置"
        }
    }
}