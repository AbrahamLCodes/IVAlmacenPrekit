package com.iv.ivalmacenprekit.features.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iv.ivalmacenprekit.apiclient.dto.AuthPrincipalResponseDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasBodyDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasResponseDto
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasResultDto
import com.iv.ivalmacenprekit.features.auth.usecases.AuthPrincipalUseCase
import com.iv.ivalmacenprekit.features.auth.usecases.AuthSaasUseCae
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authPrincipalUseCase: AuthPrincipalUseCase,
    private val authSaasUseCae: AuthSaasUseCae
) : ViewModel() {

    private val _authResponse = mutableStateOf<AuthPrincipalResponseDto>(
        AuthPrincipalResponseDto(
            emptyList()
        )
    )
    val authResponse: State<AuthPrincipalResponseDto> = _authResponse

    private val _authSaasResponse = mutableStateOf<AuthSaasResponseDto>(
        AuthSaasResponseDto(
            AuthSaasResultDto(
                false,
                0,
                0,
                0,
                0,
                "",
                ""
            )
        )
    )
    val authSaasResponse = _authSaasResponse

    fun submitLoginPrincipal(username: String, password: String) {
        viewModelScope.launch {
            val result = authPrincipalUseCase(username, password)

            if (result.isSuccess) {
                _authResponse.value = result.getOrNull()!!
            } else {
                // Handle error case, e.g., log the error or update UI state
            }
        }
    }

    fun submitLoginSaas(body: AuthSaasBodyDto) {
        viewModelScope.launch {
            val result = authSaasUseCae(body)

            if (result.isSuccess) {
                // Handle success case, e.g., update UI state
            } else {
                // Handle error case, e.g., log the error or update UI state
            }
        }
    }

}