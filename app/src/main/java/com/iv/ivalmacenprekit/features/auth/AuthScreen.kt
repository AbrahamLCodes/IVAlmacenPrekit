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

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel()) {

    val showWsLogin = remember { mutableStateOf(true) }

    var usernameWs by remember { mutableStateOf("") }
    var passwordWs by remember { mutableStateOf("") }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        val usernameState = if (showWsLogin.value) usernameWs else username
        val passwordState = if (showWsLogin.value) passwordWs else password

        val onUsernameChange: (String) -> Unit =
            { if (showWsLogin.value) usernameWs = it else username = it }
        val onPasswordChange: (String) -> Unit =
            { if (showWsLogin.value) passwordWs = it else password = it }

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Username
                OutlinedTextField(
                    value = usernameState,
                    onValueChange = onUsernameChange,
                    label = {
                        Text(
                            if (showWsLogin.value) "Usuario WS" else "Usuario",
                            fontSize = 12.sp
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                )

                // Password
                var passwordVisible by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = passwordState,
                    onValueChange = onPasswordChange,
                    label = {
                        Text(
                            if (showWsLogin.value) "Contraseña WS" else "Contraseña",
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 5.dp)
                )
            }

            // Login Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { performAuthentication(viewModel, username, password, showWsLogin) },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(if (showWsLogin.value) "Verificar" else "Iniciar")
                }
            }
        }

        // --- Logo Section ---
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
}



fun performAuthentication(
    viewModel: AuthViewModel,
    username: String,
    password: String,
    showWsLogin: MutableState<Boolean>
) {
    showWsLogin.value = !showWsLogin.value

    viewModel.submitLoginPrincipal(
        username,
        password
    )
}
