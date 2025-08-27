package com.iv.ivalmacenprekit.features.auth.usecases

import com.iv.ivalmacenprekit.features.auth.AuthRepository
import javax.inject.Inject

class AuthPrincipalUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String) =
        repository.authPrincipal()
}