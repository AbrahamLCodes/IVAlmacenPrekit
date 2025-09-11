package com.iv.ivalmacenprekit.features.PurchaseData

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iv.ivalmacenprekit.apiclient.dto.ArticuloCompraDto
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.features.shared.data.PurchasesRepository
import com.iv.ivalmacenprekit.features.shared.data.SessionPreferences
import com.iv.ivalmacenprekit.features.shared.models.PurchaseDataState
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

    private val _uiState = mutableStateOf(PurchaseDataState())
    val uiState: State<PurchaseDataState> = _uiState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var isLoading = mutableStateOf(false)
        private set

    var allArticles = mutableStateOf<List<ArticuloCompraDto>>(emptyList())
        private set

    init {
        fillArticles()
    }

    fun updateState(update: PurchaseDataState.() -> PurchaseDataState) {
        _uiState.value = _uiState.value.update()
    }

    fun showToast(message: String, type: ToastType) {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.ShowToast(message, type))
        }
    }

    fun fillArticles() {
        isLoading.value = true
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

    fun addArticle(article: ArticuloCompraDto) {
        updateState { copy(addedData = addedData + article) }
    }

    fun removeArticle(article: ArticuloCompraDto) {
        updateState { copy(addedData = addedData - article) }
    }

    fun selectArticle(article: ArticuloCompraDto?) {
        updateState { copy(selectedArticle = article, showDetailSheet = article != null) }
    }

    fun setInvoiceNumber(number: String) {
        updateState { copy(invoiceNumber = number) }
    }

    fun setPhoto(uri: Uri?) {
        updateState { copy(photoUri = uri) }
    }

    fun toggleSheet(show: Boolean) {
        updateState { copy(showSheet = show) }
    }

    fun toggleEvidenceSheet(show: Boolean) {
        updateState { copy(showEvidenceSheet = show) }
    }

    fun toggleResumeSheet(show: Boolean) {
        updateState { copy(showResumeSheet = show) }
    }

    fun showToastState(message: String, type: ToastType) {
        updateState { copy(toastVisible = true, toastMessage = message, toastType = type) }
    }

    fun dismissToast() {
        updateState { copy(toastVisible = false) }
    }

    fun updateArticle(updated: ArticuloCompraDto) {
        updateState {
            copy(
                addedData = addedData.map { if (it.idArticulo == updated.idArticulo) updated else it }
            )
        }
    }
}
