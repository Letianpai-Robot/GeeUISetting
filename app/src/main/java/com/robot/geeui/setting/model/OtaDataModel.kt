package com.robot.geeui.setting.model

import com.google.gson.annotations.SerializedName


data class OtaDataModel(
    @SerializedName("rom_version")
    val romVersion: String,

    @SerializedName("mcu_version")
    val mcuVersion: String,

    @SerializedName("whole_package_url")
    val wholeUrl: String,

    @SerializedName("md5")
    val md5: String,

    @SerializedName("upgrade_desc")
    val updateDesc: String,

    @SerializedName("byte_size")
    val byteSize: Long,

    @SerializedName("update_time")
    val updateTime: Long,

    @SerializedName("package_collection")
    val packageCollectionModel: PackageCollectionModel,

    )