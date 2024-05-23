package com.robot.geeui.setting.model
import com.google.gson.annotations.SerializedName


data class BrightnessAndModeModel(
    @SerializedName("is_auto_app_show")
    var isAutoAppShow: Int?,
    @SerializedName("volume_size")
    var volumeSize: Int?,
    @SerializedName("volume_switch")
    var volumeSwitch: Int?
)