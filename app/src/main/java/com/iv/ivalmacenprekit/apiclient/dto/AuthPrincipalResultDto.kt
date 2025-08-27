package com.iv.ivalmacenprekit.apiclient.dto

data class AuthPrincipalResultDto(
    val ftp: String,
    val ftpContrasena: String,
    val ftpPuerto: Int,
    val ftpRespaldo: String,
    val ftpRespaldoPass: String,
    val ftpRespaldoPuerto: Int,
    val ftpRespaldoUser: String,
    val ftpServer: String,
    val ftpUsuario: String,
    val sPassword: String,
    val sUUID: String,
    val sUsuario: String,
    val sWebService: String
)
