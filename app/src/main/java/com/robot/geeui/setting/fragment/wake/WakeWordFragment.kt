package com.robot.geeui.setting.fragment.wake

import android.content.Context
import android.graphics.Color
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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import com.robot.geeui.setting.model.WakeConfigChinese


class WakeWordFragment : BaseSecondFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_wakeword
    }

    private var wakeData: WakeConfigChinese? = null
    private var recyclerView: RecyclerView? = null
    val list = arrayListOf(
        ListItemModel("嗨，小乐"),
        ListItemModel("嗨，小天"),
        ListItemModel("嗨，小派"),
        ListItemModel("嗨，乐天派"),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerview)
        initRecyclerview()
        getData()
    }

    private fun getData() {
        singleExecutor.execute {
            GeeUiNetManager.get(
                context,
                true,
                "your interface url",
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.i("TAG", "onFailure: ")
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var data = response.body.string();
                        var type = object : TypeToken<BaseModel<WakeConfigChinese>>() {}.type
                        var wakeConfig = gson.fromJson<BaseModel<WakeConfigChinese>>(data, type)
                        wakeData = wakeConfig.baseData
                        list.get(0).isChecked = true
                        list.get(1).isChecked = wakeConfig.baseData.xiaotianSwitch == 1
                        list.get(2).isChecked = wakeConfig.baseData.xiaopaiSwitch == 1
                        list.get(3).isChecked = wakeConfig.baseData.letianpaiSwitch == 1
                        activity?.runOnUiThread { recyclerView?.adapter?.notifyDataSetChanged() }
                    }
                })
        }
    }

    private fun initRecyclerview() {
        val adapter = TestAdapter()
        adapter.submitList(list)
        adapter.setOnItemClickListener { adapter, view, position ->
            if (SystemUtil.isInChinese() && position != 0) {
                list[position].isChecked = !list[position].isChecked!!
                adapter.notifyItemChanged(position)
            }
            postData()
        }
        recyclerView?.adapter = adapter
    }

    private fun postData() {
        var bodyMap = mutableMapOf(
            Pair("boy_child_voice_switch", wakeData?.boyChildVoiceSwitch),
            Pair("boy_voice_switch", wakeData?.boyVoiceSwitch),
            Pair("device_id", "setting"),
            Pair("girl_child_voice_switch", wakeData?.girlChildVoiceSwitch),
            Pair("girl_voice_switch", wakeData?.girlVoiceSwitch),
            Pair("letianpai_switch", if (list[3].isChecked == true) 1 else 0),
            Pair("more_modal_switch", wakeData?.moreModalSwitch),
            Pair("robot_voice_switch", wakeData?.robotVoiceSwitch),
            Pair("xiaole_switch", 1),
            Pair("xiaopai_switch", if (list[2].isChecked == true) 1 else 0),
            Pair("xiaotian_switch", if (list[1].isChecked == true) 1 else 0),
        )
        singleExecutor.execute {
            GeeUiNetManager.post(
                context,
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
            if (position == 0) {
                holder.binding.select.setImageResource(R.drawable.icon_select_gray)
                holder.binding.itemText.visibility = View.GONE
                holder.binding.itemTextDisable.visibility = View.VISIBLE
            } else {
                holder.binding.select.setImageResource(R.drawable.icon_select)
                holder.binding.itemText.visibility = View.VISIBLE
                holder.binding.itemTextDisable.visibility = View.GONE
            }
            holder.binding.select.visibility =
                if (item?.isChecked == true) View.VISIBLE else View.GONE

        }

    }
}