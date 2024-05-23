package com.robot.geeui.setting

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.robot.geeui.setting.databinding.ActivityBasebackBinding
import com.robot.geeui.setting.fragment.*

class CommonBackActivity : BaseActivity() {
    lateinit var binding: ActivityBasebackBinding
    private var listBackBtnClickListener = arrayListOf<BackBtnClickListener>()
    var listFragment = arrayListOf<BaseFragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasebackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.included.back.setOnClickListener {
            if (listBackBtnClickListener.isNullOrEmpty()) {
                finish()
            } else {
                switchPreFragment()
                listBackBtnClickListener.last()?.onBackPress()
            }
        }
        var fragment = intent.getStringExtra("fragment")
        showFragment(fragment)
    }

    private fun showFragment(fragment: String?) {
        var ff = when (fragment) {
            resources.getString(R.string.list_wake_desc) -> {
                WakeFragment()
            }
            resources.getString(R.string.list_sleepsetting_desc) -> {
                SleepFragment()
            }
            resources.getString(R.string.list_mijia_desc) -> {
                MijiaFragment()
            }
            resources.getString(R.string.list_dateandtime_desc) -> {
                DateAndTimeFragment()
            }
            resources.getString(R.string.list_modeswitch_desc) -> {
                ModeSwitchFragment()
            }
            resources.getString(R.string.list_brightness_desc) -> {
                BrightnessFragment()
            }
            getString(R.string.list_unbindandrestore_desc) -> {
                UnbindAndRestoreFragment()
            }
            else -> {
                DefaultFragment()
            }
        }
        listFragment.add(ff)
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, ff)
        fragmentTransaction.commit()
    }

    fun switchFragment(fragment: BaseFragment) {
        listFragment.add(fragment)
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    fun switchPreFragment() {
        var removeFragment = listFragment.removeLast()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(removeFragment)
        fragmentTransaction.commit()
        if (listFragment.size == 0) {
            finish()
        }
    }

    fun addBackBtnClickListener(backBtnClickListener: BackBtnClickListener) {
        listBackBtnClickListener.add(backBtnClickListener)
    }

    fun removeBackBtnClickListener(backBtnClickListener: BackBtnClickListener) {
        if (listBackBtnClickListener.contains(backBtnClickListener)) {
            listBackBtnClickListener.remove(backBtnClickListener)
        }
    }

    interface BackBtnClickListener {
        fun onBackPress()
    }


}