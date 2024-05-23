package com.robot.geeui.setting.model

import com.robot.geeui.setting.fragment.BaseFragment

data class ListItemModel(
    val desc: String,
    val icon: Int? = 0,
    val type: String? = "activity",
    val action: Class<*>? = null,
    var isChecked: Boolean? = false,
    var packageName: String? = null,
    var clazzName: String? = null,
    var fragment: BaseFragment? = null
)
