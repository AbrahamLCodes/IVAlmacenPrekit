package com.iv.ivalmacenprekit.apiclient.dto

import com.google.gson.annotations.SerializedName

data class DataItemDto(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Descripcion")
    val descripcion: String
)
