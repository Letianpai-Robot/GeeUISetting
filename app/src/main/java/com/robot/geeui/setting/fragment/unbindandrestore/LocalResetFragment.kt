package com.robot.geeui.setting.fragment.unbindandrestore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.CommonBackActivity
import com.robot.geeui.setting.R
import com.robot.geeui.setting.fragment.BaseSecondFragment
import com.robot.geeui.setting.utils.SystemUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class LocalResetFragment : BaseSecondFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_confirm
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.findViewById<TextView>(R.id.tips)?.setText(getString(R.string.reset_local_tips))
        view?.findViewById<ImageView>(R.id.cancel)?.setOnClickListener {
            (activity as CommonBackActivity).switchPreFragment()
        }
        view?.findViewById<ImageView>(R.id.confirm_unbind)?.setOnClickListener {
            onConfirm()
        }
    }

    private fun onConfirm() {
        Thread {
            SystemUtils.restoreRobot(requireContext())
        }.start()
        //恢复出厂设置
        val recovery = Intent("android.intent.action.MASTER_CLEAR")
        recovery.setPackage("android")
        requireContext().sendBroadcast(recovery)
    }


}