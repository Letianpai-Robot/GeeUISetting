package com.robot.geeui.setting

import android.content.Intent
import android.os.Bundle
import com.robot.geeui.setting.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.included.back.setOnClickListener { finish() }
        binding.setting.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListActivity::class.java))
        }
    }

}