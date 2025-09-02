package com.iv.ivalmacenprekit.apiclient

import com.iv.ivalmacenprekit.apiclient.dto.DataComprasResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PurchasesApiService {

    @GET("getDataCompras")
    suspend fun getDataCompras(
        @Query("idSucursal") idSucursal: Int
    ): DataComprasResponse
}