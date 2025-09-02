package com.iv.ivalmacenprekit.apiclient.dto

import com.google.gson.annotations.SerializedName

data class DataComprasResult(
    @SerializedName("Almacenes")
    val almacenes: List<DataItemDto>,

    @SerializedName("Proveedores")
    val proveedores: List<DataItemDto>,

    @SerializedName("TiposCompra")
    val tiposCompra: List<DataItemDto>
)
