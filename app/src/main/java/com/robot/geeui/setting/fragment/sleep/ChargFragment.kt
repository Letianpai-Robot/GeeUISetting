package com.robot.geeui.setting.fragment.sleep

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.google.gson.reflect.TypeToken
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.R
import com.robot.geeui.setting.databinding.ItemLanguageListBinding
import com.robot.geeui.setting.fragment.BaseSecondFragment
import com.robot.geeui.setting.fragment.wake.WakeYinseFragment
import com.robot.geeui.setting.model.BaseModel
import com.robot.geeui.setting.model.ListItemModel
import com.robot.geeui.setting.model.SleepConfig
import com.robot.geeui.setting.model.WakeConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException
import java.util.ArrayList

class ChargFragment : BaseSecondFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_sleepstatus
    }

    private var sleepConfig: SleepConfig? = null
    private lateinit var list: ArrayList<ListItemModel>
    private var recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerview)
        initRecyclerview()
        getData()
    }

    private fun initRecyclerview() {
        val iniTList = arrayListOf(
            ListItemModel(getString(R.string.sleep_sleepandsnoring_desc)),
            ListItemModel(getString(R.string.sleep_screenoff_desc)),
        )
        list = iniTList
        val adapter = WakeYinseFragment.TestAdapter()
        adapter.submitList(list)
        adapter.setOnItemClickListener { adapter, view, position ->
            var lastChecked = 0
            list.forEachIndexed { index, listItemModel ->
                if (listItemModel.isChecked == true) {
                    lastChecked = index
                    list[index].isChecked = false
                }
            }
            list[position].isChecked = true
            adapter.notifyItemChanged(lastChecked)
            adapter.notifyItemChanged(position)
            sleepConfig?.sleepTimeStatusModeSwitch = if (list[1].isChecked == true) 1 else 2
            updateData()
        }
        recyclerView?.adapter = adapter
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

    private fun updateData() {
        if (sleepConfig == null) return
        var bodyMap = mutableMapOf(
            Pair("close_screen_mode_switch", sleepConfig?.closeScreenModeSwitch),
            Pair("device_id", SystemUtil.getLtpSn()),
            Pair("start_time", sleepConfig?.startTime),
            Pair("end_time", sleepConfig?.endTime),
            Pair("sleep_mode_switch", sleepConfig?.sleepModeSwitch),
            Pair("sleep_sound_mode_switch", sleepConfig?.sleepSoundModeSwitch),
            Pair("sleep_time_status_mode_switch",sleepConfig?.sleepTimeStatusModeSwitch),
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
                    }
                })
        }
    }

    private fun updateUI() {
        if (sleepConfig?.sleepTimeStatusModeSwitch == 1) {
            list[1].isChecked = true
            list[0].isChecked = false
        } else {
            list[1].isChecked = false
            list[0].isChecked = true
        }
        activity?.runOnUiThread {
            recyclerView?.adapter?.notifyDataSetChanged()
        }
    }

    class TestAdapter : BaseQuickAdapter<ListItemModel, TestAdapter.VH>() {

        // 自定义ViewHolder类
        class VH(
            parent: ViewGroup,
            val binding: ItemLanguageListBinding = ItemLanguageListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
        ) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
            // 返回一个 ViewHolder
            return VH(parent)
        }

        override fun onBindViewHolder(holder: VH, position: Int, item: ListItemModel?) {
            // 设置item数据
            holder.binding.data = item
            holder.binding.icon.setImageResource(item?.icon ?: 0)
            holder.binding.select.visibility =
                if (item?.isChecked == true) View.VISIBLE else View.GONE
        }

    }
}