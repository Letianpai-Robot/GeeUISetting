package com.robot.geeui.setting.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robot.geeui.setting.CommonBackActivity
import com.robot.geeui.setting.ListActivity
import com.robot.geeui.setting.R
import com.robot.geeui.setting.fragment.sleep.ChargFragment
import com.robot.geeui.setting.fragment.sleep.SleepSnoringFragment
import com.robot.geeui.setting.fragment.sleep.SleepTimeFragment
import com.robot.geeui.setting.model.ListItemModel

class SleepFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                getString(R.string.sleeplist_chargingstatus_desc), fragment = ChargFragment()
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.sleepdesc_snoringsound_desc), fragment = SleepSnoringFragment()
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.sleeplist_autooff_desc), fragment = SleepTimeFragment()
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