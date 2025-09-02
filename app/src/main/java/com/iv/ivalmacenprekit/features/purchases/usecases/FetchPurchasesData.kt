package com.iv.ivalmacenprekit.features.purchases.usecases

import com.iv.ivalmacenprekit.features.purchases.PurchasesRepository
import javax.inject.Inject

class FetchPurchasesUseCase @Inject constructor(
    private val repository: PurchasesRepository
) {
    suspend operator fun invoke(idSucursal: Int) = repository.fetchPurchasesData(idSucursal)
}