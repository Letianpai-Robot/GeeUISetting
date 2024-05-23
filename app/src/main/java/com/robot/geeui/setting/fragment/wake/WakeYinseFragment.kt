package com.robot.geeui.setting.fragment.wake

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
import com.robot.geeui.setting.model.BaseModel
import com.robot.geeui.setting.model.ListItemModel
import com.robot.geeui.setting.model.WakeConfigChinese
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class WakeYinseFragment : BaseSecondFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_wakeword
    }

    private var wakeData: WakeConfigChinese? = null
    private var recyclerView: RecyclerView? = null
    val list = arrayListOf(
        ListItemModel("男生"),
        ListItemModel("女生"),
        ListItemModel("男孩"),
        ListItemModel("女孩"),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerview)
        initRecyclerview()
        getData()
    }

    private fun initRecyclerview() {
        val adapter = TestAdapter()
        adapter.submitList(list)
        adapter.setOnItemClickListener { adapter, view, position ->
            var lastChecked = 0
            list.forEachIndexed { index, listItemModel ->
                if (listItemModel.isChecked == true) {
                    lastChecked = index
                    listItemModel.isChecked = false
                }
            }
            list[position].isChecked = true
            adapter.notifyItemChanged(lastChecked)
            adapter.notifyItemChanged(position)
            saveYinSe()
        }
        recyclerView?.adapter = adapter

    }

    private fun saveYinSe() {
        var bodyMap = mutableMapOf(
            Pair("boy_child_voice_switch", if (list[2].isChecked == true) 1 else 0),
            Pair("boy_voice_switch", if (list[0].isChecked == true) 1 else 0),
            Pair("device_id", "setting"),
            Pair("girl_child_voice_switch", if (list[3].isChecked == true) 1 else 0),
            Pair("girl_voice_switch", if (list[1].isChecked == true) 1 else 0),
            Pair("letianpai_switch", wakeData?.letianpaiSwitch),
            Pair("more_modal_switch", wakeData?.moreModalSwitch),
            Pair("robot_voice_switch", wakeData?.robotVoiceSwitch),
            Pair("xiaole_switch", wakeData?.xiaoleSwitch),
            Pair("xiaopai_switch", wakeData?.xiaopaiSwitch),
            Pair("xiaotian_switch", wakeData?.xiaotianSwitch),
        )
        singleExecutor.execute {
            GeeUiNetManager.post(context,
                true,
                "your interface url",
                bodyMap.toMutableMap() as HashMap,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.i("TAG", "onResponse: ${response.body.string()}")
                    }
                })
        }
    }

    private fun getData() {
        singleExecutor.execute {
            GeeUiNetManager.get(context,
                SystemUtil.isInChinese(),
                "your interface url",
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.i("TAG", "onFailure: ")
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var data = response.body.string()
                        Log.i("TAG", "onResponse: $data")
                        var type = object : TypeToken<BaseModel<WakeConfigChinese>>() {}.type
                        var wakeConfig = gson.fromJson<BaseModel<WakeConfigChinese>>(data, type)
                        Log.i("TAG", "onResponse===: ${wakeConfig.toString()}")
                        wakeData = wakeConfig.baseData
                        updateUI(wakeConfig.baseData)

//                        activity?.runOnUiThread { recyclerView?.adapter?.notifyDataSetChanged() }
                    }
                })
        }
    }

    private fun updateUI(wakeConfig: WakeConfigChinese) {
        list[0].isChecked = wakeConfig.boyVoiceSwitch == 1
        list[1].isChecked = wakeConfig.girlVoiceSwitch == 1
        list[2].isChecked = wakeConfig.boyChildVoiceSwitch == 1
        list[3].isChecked = wakeConfig.girlChildVoiceSwitch == 1

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