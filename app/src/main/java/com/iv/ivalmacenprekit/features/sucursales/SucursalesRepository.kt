package com.iv.ivalmacenprekit.features.sucursales

import com.iv.ivalmacenprekit.apiclient.AlmacenApiService
import com.iv.ivalmacenprekit.apiclient.dto.ObtenerSucursalesResponse
import javax.inject.Inject

interface SucursalesRepository {
    suspend fun fetchSucursales(): Result<ObtenerSucursalesResponse>
}

class SucursalesRepositoryImpl @Inject constructor(
    private val sucursalesApi: AlmacenApiService
) : SucursalesRepository {

    override suspend fun fetchSucursales(): Result<ObtenerSucursalesResponse> {
        return try {
            val response = sucursalesApi.obtenerSucursales()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}