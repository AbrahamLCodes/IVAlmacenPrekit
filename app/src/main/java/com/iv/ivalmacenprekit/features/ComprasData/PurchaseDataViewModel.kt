package com.iv.ivalmacenprekit.features.ComprasData

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iv.ivalmacenprekit.apiclient.dto.ArticuloCompraDto
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.features.shared.data.PurchasesRepository
import com.iv.ivalmacenprekit.features.shared.data.SessionPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseDataViewModel @Inject constructor(
    application: Application,
    private val repository: PurchasesRepository,
    private val sessionPreferences: SessionPreferences
) : AndroidViewModel(application) {

    var isLoading = mutableStateOf(false)
        private set

    private var _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var allArticles = mutableStateOf<List<ArticuloCompraDto>>(emptyList())
        private set

    init {
        fillArticles()
    }

    fun showToast(message: String, type: ToastType) {
        viewModelScope.launch {
            Log.d("", "showToast asdasdsd")
            _uiEvent.send(UiEvent.ShowToast(message, type))
        }
    }

    fun fillArticles() {

        isLoading.value = true;

        viewModelScope.launch {

            delay(500)

            val idSucursal = sessionPreferences.idSucursal

            val response = repository.fetchArticulosCompras(idSucursal)
            if (response.isSuccess) {
                val dataArticulos = response.getOrNull()!!

                allArticles.value = dataArticulos.articulos.map { articulo ->
                    articulo.copy(
                        quantity = articulo.quantity.takeIf { it > 0 } ?: 1,
                        unitPrice = articulo.unitPrice.coerceAtLeast(0.0)
                    )
                }
            } else {
                showToast("Error al obtener los datos", ToastType.DANGER)
            }

            isLoading.value = false
        }
    }
}