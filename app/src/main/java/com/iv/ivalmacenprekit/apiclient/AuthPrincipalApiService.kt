package com.iv.ivalmacenprekit.apiclient

import com.iv.ivalmacenprekit.apiclient.dto.AuthPrincipalResponseDto
import retrofit2.http.POST

interface AuthPrincipalApiService {
    @POST("validaLogin")
    suspend fun login(): AuthPrincipalResponseDto
}