package com.robot.geeui.setting.model

import com.google.gson.annotations.SerializedName


data class WakeConfigChinese(
    @SerializedName("boy_child_voice_switch")
    var boyChildVoiceSwitch: Int?,
    @SerializedName("boy_voice_switch")
    var boyVoiceSwitch: Int?,
    @SerializedName("custom_pinyin")
    var customPinyin: String?,
    @SerializedName("custom_switch")
    var customSwitch: Int?,
    @SerializedName("custom_title")
    var customTitle: String?,
    @SerializedName("dialog_language")
    var dialogLanguage: String?,
    @SerializedName("girl_child_voice_switch")
    var girlChildVoiceSwitch: Int?,
    @SerializedName("girl_voice_switch")
    var girlVoiceSwitch: Int?,
    @SerializedName("letianpai_switch")
    var letianpaiSwitch: Int?,
    @SerializedName("more_modal_switch")
    var moreModalSwitch: Int?,
    @SerializedName("robot_voice_switch")
    var robotVoiceSwitch: Int?,
    @SerializedName("selected_voice_id")
    var selectedVoiceId: String?,
    @SerializedName("selected_voice_name")
    var selectedVoiceName: String?,
    @SerializedName("xiaole_switch")
    var xiaoleSwitch: Int?,
    @SerializedName("xiaopai_switch")
    var xiaopaiSwitch: Int?,
    @SerializedName("xiaotian_switch")
    var xiaotianSwitch: Int?
)