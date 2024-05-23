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


class GlobalWakeWordFragment : BaseSecondFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_wakeword
    }

    private var recyclerView: RecyclerView? = null
    val list = arrayListOf(
        ListItemModel("Hi Rux", isChecked = true),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerview)
        initRecyclerview()
    }

    private fun initRecyclerview() {
        val adapter = TestAdapter()
        adapter.submitList(list)
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