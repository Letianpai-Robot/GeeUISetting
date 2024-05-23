package com.robot.geeui.setting.model

import com.google.gson.annotations.SerializedName


data class SleepConfig(
    @SerializedName("close_screen_mode_switch")
    var closeScreenModeSwitch: Int?,
    @SerializedName("end_time")
    var endTime: String?,
    @SerializedName("sleep_mode_switch")
    var sleepModeSwitch: Int?,
    @SerializedName("sleep_sound_mode_switch")
    var sleepSoundModeSwitch: Int?,
    @SerializedName("sleep_time_status_mode_switch")
    var sleepTimeStatusModeSwitch: Int?,
    @SerializedName("start_time")
    var startTime: String?
)