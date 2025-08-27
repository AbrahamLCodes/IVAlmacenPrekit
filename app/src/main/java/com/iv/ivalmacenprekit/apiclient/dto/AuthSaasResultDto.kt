package com.iv.ivalmacenprekit.apiclient.dto

data class AuthSaasResultDto(
    val exito: Boolean,
    val idAlmacen: Int,
    val idRuta: Int,
    val idSucursal: Int,
    val idUsuario: Int,
    val nombreRuta: String,
    val nombreSucursal: String
)