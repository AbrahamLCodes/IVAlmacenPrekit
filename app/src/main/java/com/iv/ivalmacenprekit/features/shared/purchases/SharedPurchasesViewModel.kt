package com.iv.ivalmacenprekit.features.shared.purchases

import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iv.ivalmacenprekit.apiclient.dto.ArticuloCompraDto
import com.iv.ivalmacenprekit.apiclient.dto.DataItemDto
import com.iv.ivalmacenprekit.features.purchases.usecases.FetchPurchasesUseCase
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.features.shared.data.PurchasesRepository
import com.iv.ivalmacenprekit.features.shared.data.SessionPreferences
import com.iv.ivalmacenprekit.features.shared.models.PurchaseDataState
import com.iv.ivalmacenprekit.features.shared.models.PurchaseFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import androidx.compose.runtime.State

@HiltViewModel
class SharedPurchaseViewModel @Inject constructor(
    application: Application,
    private val fetchPurchasesDataUseCase: FetchPurchasesUseCase,
    private val repository: PurchasesRepository,
    private val sessionPreferences: SessionPreferences
) : AndroidViewModel(application) {

    // ─────────────────────────────
    // Shared UI Events
    // ─────────────────────────────
    private var _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var isLoading = mutableStateOf(false)
        private set

    // ─────────────────────────────
    // FormPurchases State
    // ─────────────────────────────
    var dataAlmacenes = mutableStateOf<List<DataItemDto>>(emptyList())
        private set

    var dataProveedores = mutableStateOf<List<DataItemDto>>(emptyList())
        private set

    var dataTiposCompra = mutableStateOf<List<DataItemDto>>(emptyList())
        private set

    var formState = mutableStateOf(PurchaseFormState())
        private set

    // ─────────────────────────────
    // PurchaseData State
    // ─────────────────────────────
    private val _uiState = mutableStateOf(PurchaseDataState())
    val uiState: State<PurchaseDataState> = _uiState

    var allArticles = mutableStateOf<List<ArticuloCompraDto>>(emptyList())
        private set

    // ─────────────────────────────
    // Init
    // ─────────────────────────────
    init {
        fetchPurchasesData()
        fillArticles()
    }

    // ─────────────────────────────
    // Fetch purchase form dropdowns
    // ─────────────────────────────
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

                _uiEvent.send(UiEvent.ShowToast("Datos de compras cargados", ToastType.SUCCESS))
            } catch (e: Exception) {
                _uiEvent.send(
                    UiEvent.ShowToast("Error al cargar info: ${e.message}", ToastType.DANGER)
                )
            } finally {
                isLoading.value = false
            }
        }
    }

    // ─────────────────────────────
    // Form State Updates
    // ─────────────────────────────
    fun updateNoOrden(value: String) {
        formState.value = formState.value.copy(noOrden = value)
    }

    fun updateNoCompra(value: String) {
        formState.value = formState.value.copy(noCompra = value)
    }

    fun updateDateTime(dateTime: String, calendar: Calendar) {
        formState.value = formState.value.copy(
            selectedDateTime = dateTime,
            selectedCalendar = calendar
        )
    }

    fun updateAlmacen(almacen: DataItemDto?) {
        formState.value = formState.value.copy(selectedAlmacen = almacen)
    }

    fun updateProveedor(proveedor: DataItemDto?) {
        formState.value = formState.value.copy(selectedProveedor = proveedor)
    }

    fun updateTipoCompra(tipoCompra: DataItemDto?) {
        formState.value = formState.value.copy(selectedTipoCompra = tipoCompra)
    }

    fun updateObservaciones(value: String) {
        formState.value = formState.value.copy(observaciones = value)
    }

    fun showTimePicker(dateMillis: Long) {
        formState.value = formState.value.copy(datePickerMillis = dateMillis)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun completeDateTimeSelection(hour: Int, minute: Int) {
        formState.value.datePickerMillis?.let { dateMillis ->
            val localDate = Instant.ofEpochMilli(dateMillis)
                .atZone(ZoneId.of("UTC")) // datePicker is in UTC
                .toLocalDate()

            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, localDate.year)
                set(Calendar.MONTH, localDate.monthValue - 1)
                set(Calendar.DAY_OF_MONTH, localDate.dayOfMonth)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault())
            val zonedDateTime = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(calendar.timeInMillis),
                ZoneId.systemDefault()
            )

            formState.value = formState.value.copy(
                selectedDateTime = zonedDateTime.format(formatter),
                selectedCalendar = calendar,
                datePickerMillis = null
            )
        }
    }

    fun validateForm(): Boolean {
        val result = formState.value.noOrden.isNotBlank() &&
                formState.value.noCompra.isNotBlank() &&
                formState.value.selectedDateTime != "Selecciona fecha y hora" &&
                formState.value.selectedAlmacen != null &&
                formState.value.selectedProveedor != null &&
                formState.value.selectedTipoCompra != null

        if (!result) {
            viewModelScope.launch {
                _uiEvent.send(UiEvent.ShowToast("Llene todo el formulario ❌", ToastType.DANGER))
            }
        }
        return result
    }

    // ─────────────────────────────
    // PurchaseData Updates
    // ─────────────────────────────
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
                showToast("Error al obtener los artículos", ToastType.DANGER)
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
