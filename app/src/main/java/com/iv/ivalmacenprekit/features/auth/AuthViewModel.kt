package com.iv.ivalmacenprekit.features.auth

import android.app.Application
import android.provider.Settings
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iv.ivalmacenprekit.apiclient.dto.AuthPrincipalResponseDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasBodyDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasResponseDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasResultDto
import com.iv.ivalmacenprekit.features.auth.usecases.AuthPrincipalUseCase
import com.iv.ivalmacenprekit.features.auth.usecases.AuthSaasUseCase
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.features.shared.data.SessionPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val authPrincipalUseCase: AuthPrincipalUseCase,
    private val authSaasUseCase: AuthSaasUseCase,
    private val sessionPreferences: SessionPreferences
) : AndroidViewModel(application) {

    private val _authPrincipalResponse = mutableStateOf(
        AuthPrincipalResponseDto(emptyList())
    )
    val authPrincipalResponse: State<AuthPrincipalResponseDto> = _authPrincipalResponse

    private val _authSaasResponse = mutableStateOf(
        AuthSaasResponseDto(AuthSaasResultDto(false, 0, 0, 0, 0, "", ""))
    )
    val authSaasResponse = _authSaasResponse

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _showWsLogin = mutableStateOf(true)
    val showWsLogin: State<Boolean> = _showWsLogin

    fun submitLoginPrincipal(username: String, password: String) {
        val idAndroid = Settings.Secure.getString(
            getApplication<Application>().contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val app = 5

        viewModelScope.launch {
            _isLoading.value = true
            val result = authPrincipalUseCase(username, password, idAndroid, app)
            _isLoading.value = false

            if (result.isSuccess) {
                val response = result.getOrNull()!!

                // store the response
                _authPrincipalResponse.value = response

                // ✅ validation: check if array has 1 element and sWebService is empty
                val isInvalid = response.validaLoginResult.size == 1 &&
                        response.validaLoginResult.first().sWebService.isNullOrEmpty()

                if (isInvalid) {
                    _uiEvent.send(UiEvent.ShowToast("Credenciales inválidas ❌", ToastType.DANGER))
                } else {
                    _showWsLogin.value = false // switch to SaaS login

                    sessionPreferences.wsUrl = response.validaLoginResult.first().sWebService

                    _uiEvent.send(UiEvent.ShowToast("Login Principal exitoso 🎉", ToastType.SUCCESS))
                    _uiEvent.send(UiEvent.LoginClienteSuccess)
                }
            } else {
                _uiEvent.send(UiEvent.ShowToast("Error en la red o servidor ⚠️", ToastType.DANGER))
            }
        }
    }

    fun submitLoginSaas(body: AuthSaasBodyDto) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authSaasUseCase(body)
            _isLoading.value = false

            if (result.isSuccess) {
                _authSaasResponse.value = result.getOrNull()!!

                val response = _authSaasResponse.value

                val isValid = response.loginUsuarioResult.exito

                if (isValid) {
                    _uiEvent.send(UiEvent.LoginSaasSuccess)
                    _uiEvent.send(UiEvent.ShowToast("Login SaaS correcto ✅", ToastType.SUCCESS))

                    sessionPreferences.idUsuario = response.loginUsuarioResult.idUsuario
                    sessionPreferences.idAlmacen = response.loginUsuarioResult.idAlmacen
                    sessionPreferences.idRuta = response.loginUsuarioResult.idRuta
                    sessionPreferences.idSucursal = response.loginUsuarioResult.idSucursal
                    sessionPreferences.nombreRuta = response.loginUsuarioResult.nombreRuta
                    sessionPreferences.nombreSucursal = response.loginUsuarioResult.nombreSucursal
                } else {
                    _uiEvent.send(UiEvent.ShowToast("Credenciales SaaS inválidas ❌", ToastType.DANGER))
                }
            } else {
                _uiEvent.send(UiEvent.ShowToast("Login SaaS fallido ⚠️", ToastType.DANGER))
            }
        }
    }
}