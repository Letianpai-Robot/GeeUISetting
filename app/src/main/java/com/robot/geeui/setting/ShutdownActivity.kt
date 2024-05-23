package com.robot.geeui.setting

import android.app.Service
import android.graphics.Color
import android.os.Bundle
import android.os.PowerManager
import com.robot.geeui.setting.databinding.ActivityShutdownBinding
import com.robot.geeui.setting.model.ListItemModel
import java.lang.reflect.Method

class ShutdownActivity : BaseActivity() {
    lateinit var binding: ActivityShutdownBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShutdownBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.included.back.let {
            it.setOnClickListener {
                finish()
            }
        }
        binding.shutdown.setTextColor(Color.parseColor("#FF0000"))
        binding.reboot.setTextColor(Color.parseColor("#0256FF"))
        binding.shutdown.setOnClickListener {
            powerOff("shutdown")
        }
        binding.reboot.setOnClickListener {
            (getSystemService(Service.POWER_SERVICE) as PowerManager).reboot("geeui setting")
        }
    }

    fun powerOff(command: String) {
        val pm = getSystemService(Service.POWER_SERVICE) as PowerManager
        val clazz: Class<*> = pm.javaClass
        try {
            val shutdown: Method = clazz.getMethod(
                command,
                Boolean::class.javaPrimitiveType,
                String::class.java,
                Boolean::class.javaPrimitiveType
            )
            shutdown.invoke(pm, false, command, false)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
}