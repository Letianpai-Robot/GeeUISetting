package com.robot.geeui.setting.model
import com.google.gson.annotations.SerializedName


data class DateAndTime(
    @SerializedName("date_format")
    var dateFormat: Int?,
    @SerializedName("hour_24_switch")
    var hour24Switch: Int?,
    @SerializedName("time_zone")
    var timeZone: String?,
    @SerializedName("time_zone_name")
    var timeZoneName: String?
)