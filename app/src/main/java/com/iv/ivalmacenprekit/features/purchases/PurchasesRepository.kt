package com.iv.ivalmacenprekit.features.purchases

import com.iv.ivalmacenprekit.apiclient.PurchasesApiService
import com.iv.ivalmacenprekit.apiclient.dto.DataComprasResponse

interface PurchasesRepository {
    suspend fun fetchPurchasesData(idSucursal: Int): Result<DataComprasResponse>
}

class PurchasesRepositoryImpl(
    private val purchasesApi: PurchasesApiService
) : PurchasesRepository {

    override suspend fun fetchPurchasesData(idSucursal: Int): Result<DataComprasResponse> {
        return try {
            val response = purchasesApi.getDataCompras(idSucursal)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}