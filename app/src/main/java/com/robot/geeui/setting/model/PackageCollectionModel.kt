package com.robot.geeui.setting.model

import com.google.gson.annotations.SerializedName


data class PackageCollectionModel(
    @SerializedName("rom_package")
    val romPackage: PackageModel,
    @SerializedName("mcu_a_package")
    val mcuPackage: PackageModel)