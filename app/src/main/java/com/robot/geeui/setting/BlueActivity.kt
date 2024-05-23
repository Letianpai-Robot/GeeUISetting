package com.robot.geeui.setting

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.databinding.ActivityOtherBinding

class BlueActivity : BaseActivity() {
    lateinit var binding: ActivityOtherBinding
    var blue: BluetoothManager? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.included.back.setOnClickListener { finish() }
        blue = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        binding.switchbtn.isChecked = blue?.adapter?.isEnabled ?: false
        if (binding.switchbtn.isChecked) {
            binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchTheme)
        } else {
            binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchThemeOff)
        }
        binding.switchbtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                blue?.adapter?.enable()
                startBle()
                binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchTheme)
            } else {
                stopBle()
                blue?.adapter?.disable()
                binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchThemeOff)
            }
        }
    }

    private fun startBle() {
        try {
            val intent = Intent()
            val cn = ComponentName(
                "com.renhejia.robot.launcher",
                "com.renhejia.robot.guidelib.ble.BleService"
            )
            intent.setComponent(cn)
            startService(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopBle() {
        try {
            val intent = Intent()
            val cn = ComponentName(
                "com.renhejia.robot.launcher",
                "com.renhejia.robot.guidelib.ble.BleService"
            )
            intent.setComponent(cn)
            stopService(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}