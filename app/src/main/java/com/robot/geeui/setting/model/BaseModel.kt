package com.robot.geeui.setting.model

import com.google.gson.annotations.SerializedName

data class BaseModel<T>(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("data")
    var baseData: T,
    @SerializedName("msg")
    var msg: String?
)