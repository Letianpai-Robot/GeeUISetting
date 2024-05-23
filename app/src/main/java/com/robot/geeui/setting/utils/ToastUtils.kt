package com.robot.geeui.setting.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast

object ToastUtils {

    private var toast: Toast? = null

    fun Context.showToast(message: String, gravity: Int? = Gravity.CENTER) {
        if (toast != null) {
            toast!!.cancel() // 取消之前的Toast显示
        }
        if (toast == null) {
            toast = Toast.makeText(this.applicationContext, message, Toast.LENGTH_LONG)
        }
        toast?.setGravity(Gravity.TOP, 0, 0)
        toast?.show()
    }


}