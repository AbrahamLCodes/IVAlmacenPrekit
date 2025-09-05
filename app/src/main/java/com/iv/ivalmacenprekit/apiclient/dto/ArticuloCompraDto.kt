package com.iv.ivalmacenprekit.apiclient.dto

import com.google.gson.annotations.SerializedName

data class ArticuloCompraDto(
    @SerializedName("Codigo")
    val codigo: String,

    @SerializedName("Descripcion")
    val descripcion: String,

    @SerializedName("FactorCompraVenta")
    val factorCompraVenta: Int,

    @SerializedName("IdArticulo")
    val idArticulo: Int,

    @SerializedName("Ieps")
    val ieps: Double,

    @SerializedName("Iva")
    val iva: Double,

    @SerializedName("Nombre")
    val nombre: String,

    var quantity: Int = 1,
    var unitPrice: Double = 0.0,
    var impDiscount: Double = 0.0,
)
