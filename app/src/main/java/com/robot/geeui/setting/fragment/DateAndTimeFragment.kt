package com.robot.geeui.setting.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.CommonBackActivity
import com.robot.geeui.setting.ListActivity
import com.robot.geeui.setting.R
import com.robot.geeui.setting.fragment.dateandtime.TimeZoneFragment
import com.robot.geeui.setting.fragment.dateandtime.Twenty4Fragment
import com.robot.geeui.setting.fragment.wake.*
import com.robot.geeui.setting.model.ListItemModel

class DateAndTimeFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        initRecyclerview(recyclerView)
    }

    private fun initRecyclerview(recyclerView: RecyclerView) {
        val adapter = ListActivity.TestAdapter()

        val list = arrayListOf<ListItemModel>()
        list.add(
            ListItemModel(
                getString(R.string.dateandtime_24_desc),
                fragment = Twenty4Fragment()
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.dataandtime_timezone_desc),
                fragment = TimeZoneFragment()
            )
        )
        adapter.submitList(list)
        adapter.setOnItemClickListener { adapter, view, position ->
            var data = list.get(position)
            data.fragment?.let { (activity as CommonBackActivity).switchFragment(it) }
        }
        recyclerView.adapter = adapter
    }
}