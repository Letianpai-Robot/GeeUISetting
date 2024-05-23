package com.robot.geeui.setting.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WakeConfig(
    @SerializedName("boy_voice_switch")
    var boyVoiceSwitch: Int?,
    @SerializedName("client_id")
    var clientId: String?,
    @SerializedName("create_time")
    var createTime: Int?,
    @SerializedName("custom_pinyin")
    var customPinyin: String?,
    @SerializedName("custom_switch")
    var customSwitch: Int?,
    @SerializedName("custom_title")
    var customTitle: String?,
    @SerializedName("dialog_language")
    var dialogLanguage: String?,
    @SerializedName("girl_voice_switch")
    var girlVoiceSwitch: Int?,
    @SerializedName("more_modal_switch")
    var moreModalSwitch: Int?,
    @SerializedName("op_user_id")
    var opUserId: Int?,
    @SerializedName("rux_switch")
    var ruxSwitch: Int?,
    @SerializedName("update_time")
    var updateTime: Int?
): Parcelable