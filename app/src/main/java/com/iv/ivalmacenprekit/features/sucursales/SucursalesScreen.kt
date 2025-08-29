package com.iv.ivalmacenprekit.features.sucursales

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iv.ivalmacenprekit.apiclient.dto.SucursalDto
import com.iv.ivalmacenprekit.features.shared.customtoast.AppToast
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.navigation.Screen

@Composable
fun SucursalesScreen(
    navController: NavController,
    viewModel: SucursalesViewModel = hiltViewModel()
) {
    val sucursales by viewModel.sucursalesState
    val selectedSucursal by viewModel.selectedSucursalState // Get from ViewModel
    val isLoading by viewModel.isLoading
    val error by viewModel.errorMessage

    var toastVisible by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    toastMessage = event.message
                    toastType = event.type
                    toastVisible = true
                }

                is UiEvent.SucursalSavedSuccess -> {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Sucursal",
            fontSize = 24.sp,
            fontWeight =
                FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier
                .padding(vertical = 20.dp)
        )

        if (isLoading) {
            Text("Cargando sucursales...")
        } else if (error != null) {
            Text("Error: $error")
        } else if (sucursales.isEmpty()) {
            Text("No hay sucursales disponibles")
        } else {
            SucursalSelector(
                sucursales = sucursales,
                selectedSucursal = selectedSucursal,
                onSucursalSelected = { selected ->
                    viewModel.selectSucursal(selected)
                }
            )
        }

        Text(
            text = selectedSucursal?.nombre ?: "Seleccione una sucursal",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = {
                selectedSucursal?.let { sucursal ->
                    onSucursalSaved(sucursal, viewModel)
                }
            },
            enabled = selectedSucursal != null,
            modifier = Modifier
                .padding(top = 16.dp)
                .width(200.dp)
        ) {
            Text("Guardar")
        }
    }

    AppToast(
        message = toastMessage,
        type = toastType,
        visible = toastVisible,
        onDismiss = { toastVisible = false }
    )
}

fun onSucursalSaved(sucursal: SucursalDto, viewModel: SucursalesViewModel) {
    viewModel.saveSucursal(sucursal.idSucursal, sucursal.nombre)
}