package com.iv.ivalmacenprekit.features.purchases

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iv.ivalmacenprekit.apiclient.dto.DataItemDto
import com.iv.ivalmacenprekit.features.purchases.usecases.FetchPurchasesUseCase
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.features.shared.data.SessionPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchasesViewModel @Inject constructor(
    application: Application,
    private val fetchPurchasesDataUseCase: FetchPurchasesUseCase,
    private val sessionPreferences: SessionPreferences
) : AndroidViewModel(application) {

    private var _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var isLoading = mutableStateOf(false)
        private set

    var dataAlmacenes = mutableStateOf<List<DataItemDto>>(emptyList())
        private set

    var dataProveedores = mutableStateOf<List<DataItemDto>>(emptyList())
        private set

    var dataTiposCompra = mutableStateOf<List<DataItemDto>>(emptyList())
        private set

    init {
        fetchPurchasesData()
    }

    private fun fetchPurchasesData() {
        val idSucursal = sessionPreferences.idSucursal

        viewModelScope.launch {
            isLoading.value = true

            try {
                val resultUseCase = fetchPurchasesDataUseCase(idSucursal)
                val purchasesData = resultUseCase.getOrNull()!!

                dataAlmacenes.value = purchasesData.result.almacenes
                dataProveedores.value = purchasesData.result.proveedores
                dataTiposCompra.value = purchasesData.result.tiposCompra

                isLoading.value = false

                _uiEvent.send(
                    UiEvent.ShowToast(
                        "Datos de compras cargados exitosamente",
                        ToastType.SUCCESS
                    )
                )
            } catch (e: Exception) {
                _uiEvent.send(
                    UiEvent.ShowToast(
                        "Error al cargar info: ${e.message}",
                        ToastType.DANGER
                    )
                )
            } finally {
                isLoading.value = false
            }
        }
    }
}