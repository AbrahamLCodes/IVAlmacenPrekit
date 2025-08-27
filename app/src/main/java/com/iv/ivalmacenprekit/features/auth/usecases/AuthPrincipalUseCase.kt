package com.iv.ivalmacenprekit.features.auth.usecases

import com.iv.ivalmacenprekit.features.auth.AuthRepository
import javax.inject.Inject
import kotlin.uuid.Uuid

class AuthPrincipalUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(usuario: String, contrasena: String, uuid: String, app: Int) =
        repository.authPrincipal(
            usuario,
            contrasena,
            uuid,
            app
        )
}