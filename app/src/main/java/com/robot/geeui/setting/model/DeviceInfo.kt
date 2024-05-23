package com.robot.geeui.setting.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceInfo(
    @SerializedName("bind_status")
    var bindStatus: Int?,
    @SerializedName("bind_time")
    var bindTime: Int?,
    @SerializedName("client_id")
    var clientId: String?,
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("sn")
    var sn: String? = "",
    @SerializedName("system_version")
    var version: String? = "",
    @SerializedName("user_id")
    var userId: Int?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("group_str")
    var point: String?

) : Parcelable