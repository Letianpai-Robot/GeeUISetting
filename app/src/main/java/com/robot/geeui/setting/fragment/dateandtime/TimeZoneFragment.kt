package com.robot.geeui.setting.fragment.dateandtime

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.letianpai.robot.components.network.encryption.EncryptionUtils
import com.letianpai.robot.components.network.nets.GeeUINetworkConsts
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.R
import com.robot.geeui.setting.databinding.ItemLanguageListBinding
import com.robot.geeui.setting.fragment.BaseSecondFragment
import com.robot.geeui.setting.model.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class TimeZoneFragment : BaseSecondFragment() {
    private var timeZoneList: List<ConfigData?>? = null
    private var recyclerView: RecyclerView? = null
    private var data: DateAndTime? = null
    val list = arrayListOf<ListItemModel>(

    )

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        initRecyclerview()
        loadData()
    }

    private fun loadData() {
        singleExecutor.execute {
            GeeUiNetManager.get(
                context,
                SystemUtil.isInChinese(),
                "your interface url",
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.i("TAG", "onFailure: ")
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var json = response.body.string();
                        var type = object : TypeToken<BaseModel<DateAndTime>>() {}.type
                        var wakeConfig = gson.fromJson<BaseModel<DateAndTime>>(json, type)
                        data = wakeConfig.baseData
                        Log.i("TAG", "onResponse===: ${wakeConfig.toString()}")
                        updateUI()
                    }
                })
            val url =
                GeeUINetworkConsts.GET_COMMON_CONFIG + "?config_key=" + if (SystemUtil.isInChinese()) "mini_time_zone_city" else "mini_time_zone_city_en"
            GeeUiNetManager.get(
                context,
                SystemUtil.isInChinese(), url,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var json = response.body.string()
                        Log.i("TAG", "onResponse: $json")
                        var type = object : TypeToken<BaseModel<TimeZoneModel>>() {}.type
                        val data = gson.fromJson<BaseModel<TimeZoneModel>>(json, type)
                        timeZoneList = data.baseData.configData
                        updateUI()
                    }
                });
        }
    }

    private fun updateUI() {
        if (data != null && timeZoneList != null) {
            val currentTImeZone = data?.timeZone
            timeZoneList?.filter { it != null }?.forEach {
                list.add(
                    ListItemModel(
                        it!!.name ?: "",
                        type = it.key,
                        isChecked = it.key == currentTImeZone
                    )
                )
            }
            activity?.runOnUiThread {
                recyclerView?.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun updateData() {
        var bodyMap = mutableMapOf(
            Pair("device_id", "setting"),
            Pair("date_format", data?.dateFormat),
            Pair("hour_24_switch", data?.hour24Switch),
            Pair("time_zone", data?.timeZone),
        )
        singleExecutor.execute {
            GeeUiNetManager.post(
                context,
                SystemUtil.isInChinese(),
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

    private fun initRecyclerview() {
        val adapter = TestAdapter()

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
            data?.timeZone = list[position].type
            data?.timeZoneName = list[position].desc
            adapter.notifyItemChanged(lastChecked)
            adapter.notifyItemChanged(position)
            updateData()
        }
        recyclerView?.adapter = adapter
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