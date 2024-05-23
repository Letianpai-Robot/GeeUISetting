package com.robot.geeui.setting.fragment

import androidx.fragment.app.Fragment
import com.google.gson.Gson
import java.util.concurrent.Executors

open class BaseFragment : Fragment() {
    val singleExecutor = Executors.newSingleThreadExecutor()
    val gson = Gson()
}