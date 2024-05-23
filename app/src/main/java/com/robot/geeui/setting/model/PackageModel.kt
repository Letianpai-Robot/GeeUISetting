package com.robot.geeui.setting.model

import com.google.gson.annotations.SerializedName


data class PackageModel(
    @SerializedName("is_update")
    val isUpdate: Int,
    @SerializedName("version")
    val version: String,
    @SerializedName("rom_package_url")
    val romPackageUrl: String,
    @SerializedName("md5")
    val md5: String,
    @SerializedName("upgrade_desc")
    val upgradeDesc: String,
    @SerializedName("update_time")
    val updateTime: Long,
)