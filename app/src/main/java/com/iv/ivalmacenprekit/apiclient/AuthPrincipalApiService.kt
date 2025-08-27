package com.iv.ivalmacenprekit.apiclient

import com.iv.ivalmacenprekit.apiclient.dto.AuthPrincipalResponseDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasBodyDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthPrincipalApiService {
    @GET("validaLogin")
    suspend fun login(
        @Query("usuario") usuario: String,
        @Query("contrasena") contrasena: String,
        @Query("UUID") uuid: String,
        @Query("app") app: Int
    ): AuthPrincipalResponseDto
}