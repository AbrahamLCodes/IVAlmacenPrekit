package com.iv.ivalmacenprekit.features.auth.usecases

import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasBodyDto
import com.iv.ivalmacenprekit.features.auth.AuthRepository
import javax.inject.Inject

class AuthSaasUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(body: AuthSaasBodyDto) =
        repository.authSaas(body)
}