package com.iv.ivalmacenprekit.features.purchases

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.iv.ivalmacenprekit.R
import com.iv.ivalmacenprekit.features.shared.customtimepicker.CustomTimePickerDialog
import com.iv.ivalmacenprekit.features.shared.customtoast.AppToast
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.features.shared.genericselector.GenericSelector
import com.iv.ivalmacenprekit.navigation.Screen
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPurchasesScreen(
    navController: NavController,
    viewModel: FormPurchasesViewModel = hiltViewModel()
) {
    // Loading state from ViewModel
    val isLoading by viewModel.isLoading

    // Form state from ViewModel
    val formState by viewModel.formState

    // Date and Time picker state
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = formState.selectedCalendar.timeInMillis
    )

    // Dropdown data from ViewModel
    val dataAlmacenes by viewModel.dataAlmacenes
    val dataProveedores by viewModel.dataProveedores
    val dataTiposCompra by viewModel.dataTiposCompra

    var toastVisible by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    BackHandler { navController.navigateUp() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    toastMessage = event.message
                    toastType = event.type
                    toastVisible = true
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
        Surface(
            color = Color(0xFF7B1E3D),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Compras",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.lottie_almacen_loading) // 👈 put your .json in res/raw
                    )

                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(180.dp)
                    )

                    Text(text = "Cargando...", fontSize = 16.sp, color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = formState.noOrden,
                            onValueChange = { viewModel.updateNoOrden(it) },
                            label = { Text("No. Orden", fontSize = 12.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = formState.selectedDateTime,
                            onValueChange = { },
                            label = { Text("Fecha y hora", fontSize = 12.sp) },
                            enabled = false,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Pick date"
                                )
                            },
                            modifier = Modifier
                                .weight(2f)
                                .clickable { showDatePicker = true }
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = formState.noCompra,
                        onValueChange = { viewModel.updateNoCompra(it) },
                        label = { Text("No. Compra", fontSize = 12.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    GenericSelector(
                        dataAlmacenes,
                        formState.selectedAlmacen,
                        itemId = { it.id },
                        itemText = { it.descripcion },
                        label = "Almacén",
                        onItemSelected = { viewModel.updateAlmacen(it) }
                    )
                }

                item {
                    GenericSelector(
                        dataProveedores,
                        formState.selectedProveedor,
                        itemId = { it.id },
                        itemText = { it.descripcion },
                        label = "Proveedor",
                        onItemSelected = { viewModel.updateProveedor(it) }
                    )
                }

                item {
                    GenericSelector(
                        dataTiposCompra,
                        formState.selectedTipoCompra,
                        itemId = { it.id },
                        itemText = { it.descripcion },
                        label = "Tipo de compra",
                        onItemSelected = { viewModel.updateTipoCompra(it) }
                    )
                }

                item {
                    OutlinedTextField(
                        value = formState.observaciones,
                        onValueChange = { viewModel.updateObservaciones(it) },
                        label = { Text("Observaciones", fontSize = 12.sp) },
                        singleLine = false,
                        maxLines = 5,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                    )
                }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                if (viewModel.validateForm()) {
                                    navController.navigate(Screen.PurchaseDataScreen.route)
                                }
                            },
                            modifier = Modifier
                                .width(200.dp)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1E3D))
                        ) {
                            Text(text = "Seleccionar", color = Color.White, fontSize = 16.sp)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            showDatePicker = false
                            showTimePicker = true
                            viewModel.showTimePicker(millis)
                        }
                    }
                ) {
                    Text(
                        "Aceptar",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp, 10.dp)
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                title = {
                    Text(
                        text = "Selecciona una fecha",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp, 10.dp)
                    )
                }
            )
        }
    }

    if (showTimePicker) {
        Log.d("FormPurchasesScreen", "Rendering TimePicker - $showTimePicker")
        CustomTimePickerDialog(
            onDismiss = { showTimePicker = false },
            onTimeSelected = { hour, minute ->
                showTimePicker = false
                viewModel.completeDateTimeSelection(hour, minute)
            },
            initialHour = formState.selectedCalendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = formState.selectedCalendar.get(Calendar.MINUTE)
        )
    }

    AppToast(
        message = toastMessage,
        type = toastType,
        visible = toastVisible,
        onDismiss = { toastVisible = false }
    )
}

