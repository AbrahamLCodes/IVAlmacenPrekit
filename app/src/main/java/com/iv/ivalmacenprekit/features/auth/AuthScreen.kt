package com.iv.ivalmacenprekit.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iv.ivalmacenprekit.R
import com.iv.ivalmacenprekit.apiclient.dto.AuthSaasBodyDto
import com.iv.ivalmacenprekit.features.shared.customtoast.AppToast
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.features.shared.loadingbutton.LoadingButton

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel()) {

    val showWsLogin by viewModel.showWsLogin

    var usernameWs by remember { mutableStateOf("") }
    var passwordWs by remember { mutableStateOf("") }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var toastVisible by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }


    val isLoading by viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    toastMessage = event.message
                    toastType = event.type
                    toastVisible = true
                }
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        val usernameState = if (showWsLogin) usernameWs else username
        val passwordState = if (showWsLogin) passwordWs else password

        val onUsernameChange: (String) -> Unit =
            { if (showWsLogin) usernameWs = it else username = it }
        val onPasswordChange: (String) -> Unit =
            { if (showWsLogin) passwordWs = it else password = it }

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = usernameState,
                onValueChange = onUsernameChange,
                label = {
                    Text(if (showWsLogin) "Usuario WS" else "Usuario", fontSize = 12.sp)
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
            )

            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = passwordState,
                onValueChange = onPasswordChange,
                label = {
                    Text(if (showWsLogin) "Contraseña WS" else "Contraseña", fontSize = 12.sp)
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(if (passwordVisible) "Hide" else "Show", fontSize = 12.sp)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 5.dp)
            )

            LoadingButton(
                text = if (showWsLogin) "Verificar" else "Iniciar",
                loading = isLoading,
                onClick = {
                    performAuthentication(viewModel, usernameWs, passwordWs, showWsLogin)
                },
                modifier = Modifier.width(200.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.iv_logo_cropped),
                contentDescription = "Logo",
                modifier = Modifier.padding(50.dp)
            )
        }
    }

    AppToast(
        message = toastMessage,
        type = toastType,
        visible = toastVisible,
        onDismiss = { toastVisible = false }
    )
}


fun performAuthentication(
    viewModel: AuthViewModel,
    username: String,
    password: String,
    showWsLogin: Boolean
) {
    if (showWsLogin) {
        submitLoginPrincpal(viewModel, username, password)
    } else {
        submitLoginWs(viewModel, username, password)
    }
}

fun submitLoginPrincpal(
    viewModel: AuthViewModel,
    username: String,
    password: String,
) {
    viewModel.submitLoginPrincipal(
        username,
        password
    )
}

fun submitLoginWs(
    viewModel: AuthViewModel,
    username: String,
    password: String,
) {
    val body = AuthSaasBodyDto(
        username,
        password
    )

    viewModel.submitLoginSaas(
        body
    )
}
