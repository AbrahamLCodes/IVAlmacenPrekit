package com.iv.ivalmacenprekit.features.auth

import com.iv.ivalmacenprekit.apiclient.AlmacenApiService
import com.iv.ivalmacenprekit.apiclient.AuthPrincipalApiService
import com.iv.ivalmacenprekit.apiclient.dto.AuthPrincipalResponseDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasBodyDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasResponseDto
import javax.inject.Inject

interface AuthRepository {
    suspend fun authPrincipal(): Result<AuthPrincipalResponseDto>
    suspend fun authSaas(body: AuthSaasBodyDto): Result<AuthSaasResponseDto>
}

class AuthRepositoryImpl @Inject constructor(
    private val authPrincipalApi: AuthPrincipalApiService,
    private val almacenApi: AlmacenApiService
) : AuthRepository {

    override suspend fun authPrincipal(): Result<AuthPrincipalResponseDto> {
        return authPrincipalApi.login().let {
            Result.success(it)
        }
    }

    override suspend fun authSaas(body: AuthSaasBodyDto): Result<AuthSaasResponseDto> {
        return almacenApi.loginSaas(body).let {
            Result.success(it)
        }
    }
}