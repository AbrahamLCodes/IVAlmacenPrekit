package com.iv.ivalmacenprekit.features.purchases

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iv.ivalmacenprekit.apiclient.dto.DataItemDto
import com.iv.ivalmacenprekit.features.purchases.usecases.FetchPurchasesUseCase
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.features.shared.data.SessionPreferences
import com.iv.ivalmacenprekit.features.shared.models.PurchaseFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FormPurchasesViewModel @Inject constructor(
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


    // Form state
    var formState = mutableStateOf(PurchaseFormState())
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
        formState.value = formState.value.copy(
            datePickerMillis = dateMillis
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun completeDateTimeSelection(hour: Int, minute: Int) {
        formState.value.datePickerMillis?.let { dateMillis ->

            // Instead of using UTC millis directly, convert to LocalDate
            val localDate = Instant.ofEpochMilli(dateMillis)
                .atZone(ZoneId.of("UTC")) // datePicker is in UTC
                .toLocalDate()

            // Now build a Calendar with local zone + chosen time
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, localDate.year)
                set(Calendar.MONTH, localDate.monthValue - 1)
                set(Calendar.DAY_OF_MONTH, localDate.dayOfMonth)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val formatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm",
                Locale.getDefault()
            )
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
}