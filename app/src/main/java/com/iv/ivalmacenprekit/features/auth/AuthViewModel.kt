package com.iv.ivalmacenprekit.features.auth

import android.app.Application
import android.provider.Settings
import android.util.Log
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
                _authPrincipalResponse.value = result.getOrNull()!!
                _showWsLogin.value = false // 👈 switch to normal login mode
                _uiEvent.send(UiEvent.ShowToast("Login Principal exitoso 🎉", ToastType.SUCCESS))
            } else {
                _uiEvent.send(UiEvent.ShowToast("Login Principal fallido ❌", ToastType.DANGER))
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
                _uiEvent.send(UiEvent.ShowToast("Login SaaS correcto ✅", ToastType.SUCCESS))
            } else {
                _uiEvent.send(UiEvent.ShowToast("Login SaaS fallido ⚠️", ToastType.DANGER))
            }
        }
    }
}