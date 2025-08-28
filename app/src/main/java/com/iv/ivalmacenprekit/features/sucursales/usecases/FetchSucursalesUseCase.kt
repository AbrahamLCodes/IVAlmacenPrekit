package com.iv.ivalmacenprekit.features.sucursales.usecases

import com.iv.ivalmacenprekit.features.sucursales.SucursalesRepository
import javax.inject.Inject

class FetchSucursalesUseCase @Inject constructor(
    private val repository: SucursalesRepository
) {
    suspend operator fun invoke() = repository.fetchSucursales()
}