package com.iv.ivalmacenprekit.apiclient.dto

import com.google.gson.annotations.SerializedName

data class GetArticulosComprasResponse(
    @SerializedName("GetArticulosComprasResult")
    val articulos: List<ArticuloCompraDto>
)
