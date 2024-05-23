package com.robot.geeui.setting.model
import com.google.gson.annotations.SerializedName


data class TimeZoneModel(
    @SerializedName("config_data")
    var configData: List<ConfigData?>?,
    @SerializedName("config_key")
    var configKey: String?,
    @SerializedName("config_title")
    var configTitle: String?
)

data class ConfigData(
    @SerializedName("key")
    var key: String?,
    @SerializedName("name")
    var name: String?
)