package com.iv.ivalmacenprekit.features.ComprasData.data

data class ItemArticle(
    val code: String,
    val name: String,
    val quantity: Int,
    val unitPrice: Double,
    val impDiscount: Double
)