package com.robot.geeui.setting.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.google.gson.reflect.TypeToken
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.R
import com.robot.geeui.setting.databinding.ItemLanguageListBinding
import com.robot.geeui.setting.fragment.wake.WakeYinseFragment
import com.robot.geeui.setting.model.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException
import java.util.ArrayList

class BrightnessFragment : BaseSecondFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_sleepstatus
    }

    private var data: BrightnessAndModeModel? = null
    private lateinit var list: ArrayList<ListItemModel>
    private var recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerview)
        view?.findViewById<TextView>(R.id.tips)?.setText(getString(R.string.tips_brightness))
        initRecyclerview()
        getData()
    }

    private fun initRecyclerview() {
        val iniTList = arrayListOf(
            ListItemModel(getString(R.string.brightness_standard)),
            ListItemModel(getString(R.string.brightness_full)),
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
            data?.volumeSwitch = if (list[1].isChecked == true) 1 else 0
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
                        Log.i("TAG", "onResponse: $json")
                        var type = object : TypeToken<BaseModel<BrightnessAndModeModel>>() {}.type
                        val data = gson.fromJson<BaseModel<BrightnessAndModeModel>>(json, type)
                        this@BrightnessFragment.data = data.baseData
                        updateUI()
                    }
                })
        }
    }

    private fun updateData() {
        if (data == null) return
        var bodyMap = mutableMapOf(
            Pair("device_id", SystemUtil.getLtpSn()),
            Pair("volume_size", if (data?.volumeSwitch == 1) 100 else 50),
            Pair("volume_switch", data?.volumeSwitch),

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
                        val json = response.body.string()
                        Log.i("TAG", "onResponse: $json")
                    }
                })
        }
    }

    private fun updateUI() {
        if (data?.volumeSwitch == 0) {
            list[0].isChecked = true
            list[1].isChecked = false
        } else {
            list[0].isChecked = false
            list[1].isChecked = true
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