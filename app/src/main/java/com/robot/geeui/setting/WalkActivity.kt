package com.robot.geeui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.letianpai.robot.components.network.system.SystemUtil
import com.letianpai.robot.components.network.system.SystemUtil.REGION_LANGUAGE
import com.robot.geeui.setting.databinding.ActivityOtherBinding

class WalkActivity : BaseActivity() {
    lateinit var binding: ActivityOtherBinding
    val walke_key = "persist.sys.robot.walk"

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.included.back.setOnClickListener { finish() }
        Log.i("TAG", "onCreate walke_key: "+(SystemUtil.get(walke_key, "")))
        Log.i("TAG", "onCreate REGION_LANGUAGE: "+(SystemUtil.get(REGION_LANGUAGE, "")))
        binding.switchbtn.isChecked = SystemUtil.get(walke_key, "") == "1"
        if (binding.switchbtn.isChecked) {
            binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchTheme)
        } else {
            binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchThemeOff)
        }
        binding.switchbtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                SystemUtil.set(walke_key, "1")
                binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchTheme)
            } else {
                SystemUtil.set(walke_key, "0")
                binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchThemeOff)
            }
        }
    }


}