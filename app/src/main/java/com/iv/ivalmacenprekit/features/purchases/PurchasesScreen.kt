package com.iv.ivalmacenprekit.features.purchases

import android.app.TimePickerDialog
import android.content.res.Resources
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.iv.ivalmacenprekit.apiclient.dto.DataItemDto
import com.iv.ivalmacenprekit.features.shared.genericselector.GenericSelector
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasesScreen(
    navController: NavController,
    viewModel: PurchasesViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading

    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var selectedDateTime by remember { mutableStateOf("Select date & time") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val context = LocalContext.current

    val dataAlmacenes by viewModel.dataAlmacenes
    val dataProveedores by viewModel.dataProveedores
    val dataTiposCompra by viewModel.dataTiposCompra

    val selectedAlmacen = remember { mutableStateOf<DataItemDto?>(null) }
    val selectedProveedor = remember { mutableStateOf<DataItemDto?>(null) }
    val selectedTipoCompra = remember { mutableStateOf<DataItemDto?>(null) }

    BackHandler { navController.navigateUp() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
        // Top bar stays pinned
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
                            value = number1,
                            onValueChange = { number1 = it },
                            label = { Text("No. Orden", fontSize = 12.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = selectedDateTime,
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
                        value = number2,
                        onValueChange = { number2 = it },
                        label = { Text("No. Compra", fontSize = 12.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    GenericSelector(
                        dataAlmacenes,
                        selectedAlmacen.value,
                        itemId = { it.id },
                        itemText = { it.descripcion },
                        label = "Almacén",
                        onItemSelected = { selectedAlmacen.value = it }
                    )
                }

                item {
                    GenericSelector(
                        dataProveedores,
                        selectedProveedor.value,
                        itemId = { it.id },
                        itemText = { it.descripcion },
                        label = "Proveedor",
                        onItemSelected = { selectedProveedor.value = it }
                    )
                }

                item {
                    GenericSelector(
                        dataTiposCompra,
                        selectedTipoCompra.value,
                        itemId = { it.id },
                        itemText = { it.descripcion },
                        label = "Tipo de compra",
                        onItemSelected = { selectedTipoCompra.value = it }
                    )
                }

                item {
                    OutlinedTextField(
                        value = number2,
                        onValueChange = { number2 = it },
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
                            onClick = { /* TODO: Handle submit action */ },
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

    // Date picker dialog (unchanged)
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            showDatePicker = false

                            val cal = Calendar.getInstance()
                            cal.timeInMillis = millis

                            TimePickerDialog(
                                context,
                                { _, hour: Int, minute: Int ->
                                    cal.set(Calendar.HOUR_OF_DAY, hour)
                                    cal.set(Calendar.MINUTE, minute)

                                    val formatter =
                                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                    selectedDateTime = formatter.format(cal.time)
                                },
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

