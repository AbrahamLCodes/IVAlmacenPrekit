package com.iv.ivalmacenprekit.features.shared.data

import com.iv.ivalmacenprekit.apiclient.PurchasesApiService
import com.iv.ivalmacenprekit.apiclient.dto.DataComprasResponse
import com.iv.ivalmacenprekit.apiclient.dto.GetArticulosComprasResponse

interface PurchasesRepository {
    suspend fun fetchPurchasesData(idSucursal: Int): Result<DataComprasResponse>

    suspend fun fetchArticulosCompras(idSucursal: Int): Result<GetArticulosComprasResponse>
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

    override suspend fun fetchArticulosCompras(idSucursal: Int): Result<GetArticulosComprasResponse> {
        return try {
            val response = purchasesApi.getArticulosCompras(idSucursal)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}