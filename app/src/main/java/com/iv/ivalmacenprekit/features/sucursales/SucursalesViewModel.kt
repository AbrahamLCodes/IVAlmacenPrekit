package com.iv.ivalmacenprekit.features.sucursales

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iv.ivalmacenprekit.apiclient.dto.SucursalDto
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.features.shared.data.SessionPreferences
import com.iv.ivalmacenprekit.features.sucursales.usecases.FetchSucursalesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SucursalesViewModel @Inject constructor(
    application: Application,
    private val fetchSucursalesUseCase: FetchSucursalesUseCase,
    private val sessionPreferences: SessionPreferences
) : AndroidViewModel(application) {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var sucursalesState = mutableStateOf<List<SucursalDto>>(emptyList())
        private set

    var selectedSucursalState = mutableStateOf<SucursalDto?>(null)
        private set

    var isLoading = mutableStateOf(true)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    init {
        fetchSucursales()
    }

    private fun fetchSucursales() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val resultUseCase = fetchSucursalesUseCase()
                val sucursales = resultUseCase.getOrNull()!!.obtenerSucursalesResult
                sucursalesState.value = sucursales

                if (sucursales.isNotEmpty()) {
                    selectedSucursalState.value = sucursales.first()
                }

                errorMessage.value = null
                _uiEvent.send(
                    UiEvent.ShowToast(
                        "Sucursales cargadas exitosamente",
                        ToastType.SUCCESS
                    )
                )
            } catch (e: Exception) {
                _uiEvent.send(
                    UiEvent.ShowToast(
                        "Error al cargar sucursales: ${e.message}",
                        ToastType.DANGER
                    )
                )
            } finally {
                isLoading.value = false
            }
        }
    }

    fun selectSucursal(sucursal: SucursalDto) {
        selectedSucursalState.value = sucursal
    }

    fun saveSucursal(idSucursal: Int, nombreSucursal: String) {
        viewModelScope.launch {
            sessionPreferences.idSucursal = idSucursal
            sessionPreferences.nombreSucursal = nombreSucursal

            _uiEvent.send(
                UiEvent.ShowToast(
                    "Sucursal guardada: $nombreSucursal",
                    ToastType.SUCCESS
                )
            )

            delay(1000)

            _uiEvent.send(
                UiEvent.SucursalSavedSuccess
            )
        }
    }
}