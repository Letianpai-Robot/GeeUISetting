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
import com.robot.geeui.setting.model.WakeConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class GlobalWakeYinseFragment : BaseSecondFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_wakeword
    }

    private var wakeData: WakeConfig? = null
    private var recyclerView: RecyclerView? = null
    val list = arrayListOf(
        ListItemModel("Female Voice"),
        ListItemModel("Male Voice"),
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
            Pair("boy_voice_switch", if (list[1].isChecked == true) 1 else 0),
            Pair("client_id", "setting"),
            Pair("create_time", wakeData?.createTime),
            Pair("custom_pinyin", wakeData?.customPinyin),
            Pair("custom_switch", wakeData?.customSwitch),
            Pair("custom_title", wakeData?.customTitle),
            Pair("girl_voice_switch", if (list[0].isChecked == true) 1 else 0),
            Pair("op_user_id", wakeData?.opUserId),
            Pair("rux_switch", wakeData?.ruxSwitch),
            Pair("more_modal_switch", wakeData?.moreModalSwitch),
        )
        singleExecutor.execute {
            GeeUiNetManager.post(context,
                false,
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
                        var type = object : TypeToken<BaseModel<WakeConfig>>() {}.type
                        var wakeConfig = gson.fromJson<BaseModel<WakeConfig>>(data, type)
                        Log.i("TAG", "onResponse===: ${wakeConfig.toString()}")
                        wakeData = wakeConfig.baseData
                        updateUI(wakeConfig.baseData)
                    }
                })
        }
    }

    private fun updateUI(wakeConfig: WakeConfig) {
        list[0].isChecked = wakeConfig.girlVoiceSwitch == 1
        list[1].isChecked = wakeConfig.boyVoiceSwitch == 1
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