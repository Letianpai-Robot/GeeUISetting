package com.robot.geeui.setting.fragment.wake

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

class RuxLaunageFragment : BaseSecondFragment() {
    private var recyclerView: RecyclerView? = null

    val list = arrayListOf(
        ListItemModel("English", type = "en-US"),
        ListItemModel("Italian", type = "it-IT"),
        ListItemModel("German", type = "de-DE"),
        ListItemModel("French", type = "fr-FR"),
        ListItemModel("Spanish", type = "es-ES"),
        ListItemModel("Russian", type = "ru-RU"),
        ListItemModel("Korean", type = "ko-KR"),
        ListItemModel("Japanese", type = "ja-JP"),
        ListItemModel("Portuguese", type = "pt-PT"),
        ListItemModel("Vietnam", type = "vi-VN"),
        ListItemModel("Thai", type = "th-TH"),
        ListItemModel("Ukrainian", type = "uk-UA"),//乌克兰
        ListItemModel("Czech", type = "cs-CZ"),//捷克
        ListItemModel("Dutch", type = "nl-NL"),//荷兰
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
            GeeUiNetManager.get(context,
                SystemUtil.isInChinese(),
                "your interface url",
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var data = response.body.string();
                        var type = object : TypeToken<BaseModel<WakeConfig>>() {}.type
                        var wakeConfig = gson.fromJson<BaseModel<WakeConfig>>(data, type)
                        Log.i("TAG", "onResponse===: ${wakeConfig.toString()}")
                        wakeConfig.baseData.dialogLanguage?.let { updateSelectLanguage(it) }
                    }
                });
        }
    }

    private fun updateLanguage(type: String) {
        var hashmap = HashMap<String, String>()
        hashmap.put("sn", "${Build.getSerial()}")
        hashmap.put("ts", "${EncryptionUtils.getTs()}")
        hashmap.put("dialog_language", "$type")
        singleExecutor.execute {
            GeeUiNetManager.post(context,
                SystemUtil.isInChinese(),
                "your interface url",
                hashmap,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.i("TAG", "1=1=onResponse: :${response.body.string()}")
                    }
                })
        }
    }

    private fun updateSelectLanguage(dialogLanguage: String) {
        list.forEachIndexed { index, listItemModel ->
            if (listItemModel.type == dialogLanguage) {
                Log.i("TAG", "updateSelectLanguage: $dialogLanguage  $index")
                activity?.runOnUiThread {
                    list[index].isChecked = true
                    (recyclerView?.adapter as TestAdapter).submitList(list);
                }
                return@forEachIndexed
            }
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
            adapter.notifyItemChanged(lastChecked)
            adapter.notifyItemChanged(position)
            updateLanguage(list[position].type!!)
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