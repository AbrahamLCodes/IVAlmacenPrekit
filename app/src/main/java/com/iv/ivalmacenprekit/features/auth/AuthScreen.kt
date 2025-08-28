package com.iv.ivalmacenprekit.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import com.iv.ivalmacenprekit.navigation.Screen

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val showWsLogin by viewModel.showWsLogin

    var usernameWs by remember { mutableStateOf("") }
    var passwordWs by remember { mutableStateOf("") }

    var usernameSaas by remember { mutableStateOf("") }
    var passwordSaas by remember { mutableStateOf("") }

    var toastVisible by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    val isLoading by viewModel.isLoading

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    toastMessage = event.message
                    toastType = event.type
                    toastVisible = true
                }

                is UiEvent.LoginClienteSuccess -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                is UiEvent.LoginSaasSuccess -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()

                    navController.navigate(Screen.Sucursales.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            }
    ) {
        val usernameState = if (showWsLogin) usernameWs else usernameSaas
        val passwordState = if (showWsLogin) passwordWs else passwordSaas

        val onUsernameChange: (String) -> Unit =
            { if (showWsLogin) usernameWs = it else usernameSaas = it }
        val onPasswordChange: (String) -> Unit =
            { if (showWsLogin) passwordWs = it else passwordSaas = it }

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp), // inner padding for spacing
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 👇 Header inside card
                    Text(
                        text = if (showWsLogin) "Login Cliente" else "Login SAAS",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    OutlinedTextField(
                        value = usernameState,
                        onValueChange = onUsernameChange,
                        label = {
                            Text(
                                if (showWsLogin) "Usuario Cliente" else "Usuario SAAS",
                                fontSize = 12.sp
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = Color.Gray,
                            cursorColor = Color.Gray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )

                    var passwordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = passwordState,
                        onValueChange = onPasswordChange,
                        label = {
                            Text(
                                if (showWsLogin) "Contraseña Cliente" else "Contraseña SAAS",
                                fontSize = 12.sp
                            )
                        },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            TextButton(onClick = { passwordVisible = !passwordVisible }) {
                                Text(if (passwordVisible) "Hide" else "Show", fontSize = 12.sp)
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = Color.Gray,
                            cursorColor = Color.Gray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    )

                    LoadingButton(
                        text = if (showWsLogin) "Verificar" else "Iniciar",
                        loading = isLoading,
                        onClick = {
                            keyboardController?.hide()

                            var usernameVal = usernameWs
                            var passwordVal = passwordWs

                            if (!showWsLogin) {
                                usernameVal = usernameSaas
                                passwordVal = passwordSaas
                            }

                            performAuthentication(
                                viewModel,
                                usernameVal,
                                passwordVal,
                                showWsLogin
                            )
                        },
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .width(200.dp)
                    )
                }
            }
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
