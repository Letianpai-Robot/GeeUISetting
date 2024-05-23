package com.robot.geeui.setting.fragment.sleep

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.robot.geeui.setting.CommonBackActivity
import com.robot.geeui.setting.R
import com.robot.geeui.setting.fragment.BaseSecondFragment
import com.robot.geeui.setting.fragment.sleep.time.AutoFragment
import com.robot.geeui.setting.fragment.sleep.time.SelectEndFragment
import com.robot.geeui.setting.fragment.sleep.time.SelectStartFragment
import com.robot.geeui.setting.fragment.wake.WakeYinseFragment
import com.robot.geeui.setting.model.ListItemModel

class SleepTimeFragment : BaseSecondFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview
    }

    private var recyclerView: RecyclerView? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerview)
        initRecyclerview()
    }

    private fun initRecyclerview() {
        val list = arrayListOf(
            ListItemModel(getString(R.string.time_timerswitch_desc), fragment = AutoFragment()),
            ListItemModel(
                getString(R.string.time_starttime_desc),
                fragment = SelectStartFragment()
            ),
            ListItemModel(getString(R.string.time_endtime_desc), fragment = SelectEndFragment()),
        )
        val adapter = WakeYinseFragment.TestAdapter()
        adapter.submitList(list)
        adapter.setOnItemClickListener { adapter, view, position ->
            var data = list.get(position)
            data.fragment?.let { (activity as CommonBackActivity).switchFragment(it) }
        }
        recyclerView?.adapter = adapter
    }
}