package com.robot.geeui.setting

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.databinding.ActivityListBinding
import com.robot.geeui.setting.databinding.ItemListBinding
import com.robot.geeui.setting.model.ListItemModel
import com.robot.geeui.setting.utils.ToastUtils.showToast

class ListActivity : BaseActivity() {
    lateinit var binding: ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtil.setAppLanguage(this)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.included.back.setOnClickListener { finish() }
        initRecyclerview()
    }

    private fun initRecyclerview() {
        val adapter = TestAdapter()

        val list = arrayListOf<ListItemModel>()
        list.add(
            ListItemModel(
                getString(R.string.list_about_desc),
                icon = R.drawable.icon_about,
                action = DeviceInfoActivity::class.java
            )
        )

        list.add(
            ListItemModel(
                getString(R.string.list_wifi_desc),
                icon = R.drawable.icon_wifi,
                action = WifiActivity::class.java
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.list_sound_desc),
                icon = R.drawable.icon_sound,
                action = SoundActivity::class.java
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.list_bluetooth_desc),
                icon = R.drawable.icon_blue,
                action = BlueActivity::class.java
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.list_wake_desc),
                icon = R.drawable.icon_blue,
                action = CommonBackActivity::class.java
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.list_sleepsetting_desc),
                icon = R.drawable.icon_blue,
                action = CommonBackActivity::class.java
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.list_dateandtime_desc),
                icon = R.drawable.icon_blue,
                action = CommonBackActivity::class.java
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.list_modeswitch_desc),
                icon = R.drawable.icon_blue,
                action = CommonBackActivity::class.java
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.list_brightness_desc),
                icon = R.drawable.icon_blue,
                action = CommonBackActivity::class.java
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.list_shutdown_desc),
                icon = R.drawable.icon_shutdown,
                action = ShutdownActivity::class.java
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.list_update_desc),
                icon = R.drawable.icon_update,
                action = UpdateActivity::class.java
            )
        )
//        list.add(
//            ListItemModel(
//                getString(R.string.list_resetting_desc),
//                icon = R.drawable.icon_resetting,
//                action = ResettingActivity::class.java
//            )
//        )
        list.add(
            ListItemModel(
                getString(R.string.list_unbindandrestore_desc),
                icon = R.drawable.icon_resetting,
                action = CommonBackActivity::class.java
            )
        )
        list.add(
            ListItemModel(
                getString(R.string.list_calibration_desc),
                icon = R.drawable.icon_calibration,
                type = "otherActivity",
                packageName = "com.letianpai.ltp_factory_test2",
                clazzName = "com.letianpai.ltp_factory_test2.CalibrationActivity"
            )
        )
        if (SystemUtil.isInChinese()) {
            list.add(
                ListItemModel(
                    getString(R.string.list_mijia_desc),
                    icon = R.drawable.icon_calibration,
                    action = CommonBackActivity::class.java
                )
            )
        }

        adapter.submitList(list)
        adapter.setOnItemClickListener { adapter, view, position ->
            var data = list.get(position)
            when (data.type) {
                "activity" -> {
                    startActivity(
                        Intent(
                            this@ListActivity,
                            data.action
                        ).apply { putExtra("fragment", data.desc) })
                }
                "otherActivity" -> {
                    try {
                        startActivity(Intent().apply {
                            if (data.packageName != null && data.clazzName != null) {
                                component = ComponentName(data.packageName!!, data.clazzName!!)
                            }
                        })
                    } catch (e: Exception) {
                        showToast("App not installed")
                    }
                }
                else -> {
                    var t = Toast.makeText(
                        this@ListActivity,
                        "${data.desc}${data.type}",
                        Toast.LENGTH_SHORT
                    )
                    t.setGravity(Gravity.CENTER, 0, 0)
                    t.show()
                }
            }
        }
        binding.recyclerview.adapter = adapter
    }

    private fun openOta() {
        val intent = Intent()
        val cn = ComponentName(
            "com.letianpai.otaservice", "com.letianpai.otaservice.ota.GeeUpdateService"
        )
        intent.component = cn
        startService(intent)
    }

    class TestAdapter : BaseQuickAdapter<ListItemModel, TestAdapter.VH>() {

        // 自定义ViewHolder类
        class VH(
            parent: ViewGroup,
            val binding: ItemListBinding = ItemListBinding.inflate(
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
        }

    }
}