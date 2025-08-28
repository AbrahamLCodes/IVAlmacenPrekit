package com.iv.ivalmacenprekit.apiclient

import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasBodyDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthPrincipalResponseDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasResponseDto
import com.iv.ivalmacenprekit.apiclient.dto.ObtenerSucursalesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AlmacenApiService {
    @POST("loginUsuario")
    suspend fun loginSaas(@Body body: AuthSaasBodyDto): AuthSaasResponseDto

    @GET("traeSucursales")
    suspend fun obtenerSucursales(): ObtenerSucursalesResponse
}