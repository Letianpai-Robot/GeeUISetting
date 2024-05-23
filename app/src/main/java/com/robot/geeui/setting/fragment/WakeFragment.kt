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
import com.robot.geeui.setting.fragment.wake.*
import com.robot.geeui.setting.model.ListItemModel

class WakeFragment : BaseFragment() {
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
        if (SystemUtil.isInChinese()) {

            list.add(
                ListItemModel(
                    getString(R.string.wake_action_desc),
                    fragment = DuoMoTaiFragment()
                )
            )
            list.add(
                ListItemModel(
                    getString(R.string.wake_wake_desc),
                    fragment = WakeWordFragment()
                )
            )
            list.add(
                ListItemModel(
                    getString(R.string.wake_wakeword_desc),
                    fragment = CustomeWakeFragment()
                )
            )
            list.add(
                ListItemModel(
                    getString(R.string.wake_timbre_desc),
                    fragment = WakeYinseFragment()
                )
            )
        } else {
            list.add(
                ListItemModel(
                    getString(R.string.wake_action_desc),
                    fragment = GlobalDuoMoTaiFragment()
                )
            )
            list.add(
                ListItemModel(
                    getString(R.string.wake_language_desc),
                    fragment = RuxLaunageFragment()
                )
            )
            list.add(
                ListItemModel(
                    getString(R.string.wake_wake_desc),
                    fragment = GlobalWakeWordFragment()
                )
            )
            list.add(
                ListItemModel(
                    getString(R.string.wake_timbre_desc),
                    fragment = GlobalWakeYinseFragment()
                )
            )
        }
        adapter.submitList(list)
        adapter.setOnItemClickListener { adapter, view, position ->
            var data = list.get(position)
            data.fragment?.let { (activity as CommonBackActivity).switchFragment(it) }
        }
        recyclerView.adapter = adapter
    }
}