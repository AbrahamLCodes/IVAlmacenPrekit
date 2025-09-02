package com.iv.ivalmacenprekit.apiclient.dto

import com.google.gson.annotations.SerializedName

data class DataComprasResponse(
    @SerializedName("getDataComprasResult")
    val result: DataComprasResult
)
