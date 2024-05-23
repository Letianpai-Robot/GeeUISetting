package com.robot.geeui.setting.fragment.unbindandrestore

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.CommonBackActivity
import com.robot.geeui.setting.R
import com.robot.geeui.setting.fragment.BaseSecondFragment
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class RestoreFragment : BaseSecondFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_confirm
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.findViewById<TextView>(R.id.tips)?.setText(getString(R.string.reset_tips))
        view?.findViewById<ImageView>(R.id.cancel)?.setOnClickListener {
            (activity as CommonBackActivity).switchPreFragment()
        }
        view?.findViewById<ImageView>(R.id.confirm_unbind)?.setOnClickListener {
            onConfirm()
        }

    }

    private fun onConfirm() {
        restore()
    }

    private fun restore() {
        singleExecutor.execute {
            val map = HashMap<String, String>()
            map.put("device_id", SystemUtil.getLtpSn())
            GeeUiNetManager.post(requireContext(),
                SystemUtil.isInChinese(),
                "your interface url",
                map,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {

                    }
                })
        }
    }
}