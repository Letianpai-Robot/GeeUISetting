package com.robot.geeui.setting

import android.os.Bundle
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.databinding.ActivityOtherBinding

class OtherActivity : BaseActivity() {
    lateinit var binding: ActivityOtherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.included.back.setOnClickListener { finish() }

        binding.switchbtn.isChecked = SystemUtil.getTitleTouchStatus()
        if (binding.switchbtn.isChecked) {
            binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchTheme)
        } else {
            binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchThemeOff)
        }
        binding.switchbtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                SystemUtil.openTitleTouchStatus()
                binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchTheme)
            } else {
                binding.switchbtn.setSwitchTextAppearance(this, R.style.SwitchThemeOff)
                SystemUtil.closeTitleTouchStatus()
            }
        }
    }
}