package com.iv.ivalmacenprekit.apiclient

import com.iv.ivalmacenprekit.apiclient.dto.DataComprasResponse
import com.iv.ivalmacenprekit.apiclient.dto.GetArticulosComprasResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PurchasesApiService {

    @GET("getDataCompras")
    suspend fun getDataCompras(
        @Query("idSucursal") idSucursal: Int
    ): DataComprasResponse

    @GET("getArticulosCompras")
    suspend fun getArticulosCompras(
        @Query("idSucursal") idSucursal: Int
    ): GetArticulosComprasResponse

}