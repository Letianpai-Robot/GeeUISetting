package com.robot.geeui.setting.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.robot.geeui.setting.CommonBackActivity
import com.robot.geeui.setting.R
import java.util.concurrent.Executors

open class BaseSecondFragment : BaseFragment() {

    var backBtnClickListener = object :
        CommonBackActivity.BackBtnClickListener {
        override fun onBackPress() {
            onActivityBackPress()
        }
    }

    fun onActivityBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = getLayoutId()
        return if (layoutId == 0) {
            fragmentView()
        } else {
            inflater.inflate(layoutId, null)
        }
    }

    open fun fragmentView(): View? {
        return null;
    }

    open fun getLayoutId(): Int {
        return 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CommonBackActivity).addBackBtnClickListener(backBtnClickListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as CommonBackActivity).removeBackBtnClickListener(backBtnClickListener)
    }

}