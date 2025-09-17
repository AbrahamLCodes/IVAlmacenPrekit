package com.iv.ivalmacenprekit.features.PurchaseData

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.iv.ivalmacenprekit.R
import com.iv.ivalmacenprekit.apiclient.dto.ArticuloCompraDto
import com.iv.ivalmacenprekit.features.PurchaseData.modals.EvidenceModal
import com.iv.ivalmacenprekit.features.PurchaseData.modals.PurchaseArticleSelectionModal
import com.iv.ivalmacenprekit.features.PurchaseData.modals.ResumeModalBottomSheet
import com.iv.ivalmacenprekit.features.shared.customtoast.AppToast
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import com.iv.ivalmacenprekit.features.shared.purchases.SharedPurchaseViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PurchaseDataScreen(
    navController: NavController,
    viewModel: SharedPurchaseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val isLoading by viewModel.isLoading
    val allArticles by viewModel.allArticles

    var currentDateTime by remember { mutableStateOf("") }

    // Listen to UiEvents (like Toast)
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowToast) {
                viewModel.showToastState(event.message, event.type)
            }
        }
    }

    LaunchedEffect(uiState.showEvidenceSheet) {
        if (uiState.showEvidenceSheet) {
            currentDateTime = java.time.format.DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm")
                .format(java.time.LocalDateTime.now())
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
        // Header
        Surface(color = Color(0xFF7B1E3D), modifier = Modifier.fillMaxWidth()) {
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

                IconButton(
                    onClick = { viewModel.toggleSheet(true) },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }

        // Loading state
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_almacen_loading))
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(180.dp)
                    )
                    Text(text = "Cargando...", fontSize = 16.sp, color = Color.Gray)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.toggleEvidenceSheet(true) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.svg_camera),
                            contentDescription = "Evidencia",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Evidencia")
                    }

                    Button(
                        onClick = {
                            submitPurchaseData(
                                uiState.invoiceNumber,
                                uiState.photoUri,
                                uiState.addedData,
                                onValidPurchase = { viewModel.toggleResumeSheet(true) },
                                viewModel
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.svg_save),
                            contentDescription = "Guardar",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Total
                val total =
                    uiState.addedData.sumOf { it.quantity * it.unitPrice * (1 - it.impDiscount / 100) }
                Text(
                    text = "Total: $${String.format("%.2f", total)}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Desliza a la izquierda para más acciones",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    textAlign = TextAlign.Center
                )

                // Table header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "COD",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "NOM",
                        modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "CANT",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "C/U",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "IMP",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Divider(color = Color.Gray, thickness = 1.dp)

                // Items
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(uiState.addedData, key = { it.codigo }) { item ->
                        SwipeableRow(
                            item = item,
                            onInfo = { viewModel.selectArticle(item) },
                            onDelete = { viewModel.removeArticle(item) },
                            onUpdate = { viewModel.updateArticle(it) }
                        )
                    }
                }
            }

            // BottomSheets
            PurchaseArticleSelectionBottomSheet(
                showSheet = uiState.showSheet,
                onDismiss = { viewModel.toggleSheet(false) },
                onAddArticle = { viewModel.addArticle(it) },
                onRemoveArticle = { viewModel.removeArticle(it) },
                allData = allArticles,
                actualData = uiState.addedData
            )

            EvidenceBottomSheet(
                showSheet = uiState.showEvidenceSheet,
                onDismiss = { viewModel.toggleEvidenceSheet(false) },
                invoiceNumber = uiState.invoiceNumber,
                onInvoiceNumberChange = { viewModel.setInvoiceNumber(it) },
                currentDateTime = currentDateTime,
                photoUri = uiState.photoUri,
                onPhotoTaken = { viewModel.setPhoto(it) },
                onResetPhoto = { viewModel.setPhoto(null) }
            )

            ResumePurchaseBottomSheet(
                showSheet = uiState.showResumeSheet,
                onDismiss = { viewModel.toggleResumeSheet(false) },
                addedArticles = uiState.addedData,
                onContinue = { viewModel.showToast("Compra guardada", ToastType.SUCCESS) },
                uri = uiState.photoUri
            )

            if (uiState.showDetailSheet && uiState.selectedArticle != null) {
                val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                var discountText by remember { mutableStateOf(uiState.selectedArticle!!.impDiscount.toString()) }

                ModalBottomSheet(
                    onDismissRequest = { viewModel.selectArticle(null) },
                    sheetState = detailSheetState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            "Detalles del Artículo",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(20.dp))

                        @Composable
                        fun LabeledText(label: String, value: String) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(label, fontWeight = FontWeight.Bold)
                                Text(value)
                            }
                        }

                        val selectedArticle = uiState.selectedArticle!!
                        LabeledText("Código:", selectedArticle.codigo)
                        Spacer(Modifier.height(8.dp))
                        LabeledText("Nombre:", selectedArticle.nombre)
                        Spacer(Modifier.height(8.dp))
                        LabeledText("Cantidad:", "${selectedArticle.quantity}")
                        Spacer(Modifier.height(8.dp))
                        LabeledText(
                            "Precio Unitario:",
                            "$${String.format("%.2f", selectedArticle.unitPrice)}"
                        )
                        Spacer(Modifier.height(8.dp))
                        LabeledText(
                            "Importe:",
                            "$${
                                String.format(
                                    "%.2f",
                                    selectedArticle.quantity * selectedArticle.unitPrice
                                )
                            }"
                        )
                        Spacer(Modifier.height(20.dp))

                        OutlinedTextField(
                            value = discountText,
                            onValueChange = {
                                discountText = it
                                val discount = it.toDoubleOrNull() ?: 0.0
                                viewModel.updateArticle(selectedArticle.copy(impDiscount = discount))
                            },
                            label = { Text("Descuento (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(20.dp))
                        Text(
                            text = "Importe con Descuento: $${
                                String.format(
                                    "%.2f",
                                    selectedArticle.quantity * selectedArticle.unitPrice * (1 - selectedArticle.impDiscount / 100)
                                )
                            }",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }

    AppToast(
        message = uiState.toastMessage,
        type = uiState.toastType,
        visible = uiState.toastVisible,
        onDismiss = { viewModel.dismissToast() }
    )
}


fun submitPurchaseData(
    invoiceNumber: String,
    photoUri: Uri?,
    addedMockData: List<ArticuloCompraDto>,
    onValidPurchase: () -> Unit,
    viewModel: SharedPurchaseViewModel
) {
    if (validatePurchase(invoiceNumber, photoUri, addedMockData)) {
        onValidPurchase()
    } else {
        viewModel.showToast("Faltan datos para completar la compra", ToastType.DANGER)
    }
}

fun validatePurchase(
    invoiceNumber: String,
    photoUri: Uri?,
    addedArticles: List<ArticuloCompraDto>
): Boolean {
    return invoiceNumber.isNotBlank() && photoUri != null && addedArticles.isNotEmpty()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseArticleSelectionBottomSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    onAddArticle: (ArticuloCompraDto) -> Unit,
    onRemoveArticle: (ArticuloCompraDto) -> Unit,
    allData: List<ArticuloCompraDto>,
    actualData: List<ArticuloCompraDto>
) {
    if (showSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                PurchaseArticleSelectionModal(
                    onDismiss = onDismiss,
                    repositoryData = allData,
                    actualData = actualData,
                    onAddArticle = onAddArticle,
                    onRemoveArticle = onRemoveArticle
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvidenceBottomSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    invoiceNumber: String,
    onInvoiceNumberChange: (String) -> Unit,
    currentDateTime: String,
    photoUri: Uri?,
    onPhotoTaken: (Uri) -> Unit,
    onResetPhoto: () -> Unit
) {
    if (showSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                EvidenceModal(
                    invoiceNumber = invoiceNumber,
                    onInvoiceNumberChange = onInvoiceNumberChange,
                    currentDateTime = currentDateTime,
                    photoUri = photoUri,
                    onPhotoTaken = onPhotoTaken,
                    onResetPhoto = onResetPhoto
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableRow(
    item: ArticuloCompraDto,
    onInfo: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: (ArticuloCompraDto) -> Unit
) {
    val swipeState = rememberSwipeableState(initialValue = 0)
    val swipeDistance = with(LocalDensity.current) { 200.dp.toPx() }
    val anchors = mapOf(0f to 0, -swipeDistance to 1)

    var qtyText by remember(item) { mutableStateOf(item.quantity.toString()) }
    var priceText by remember(item) { mutableStateOf(item.unitPrice.toString()) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(with(LocalDensity.current) { swipeDistance.toDp() })
                .align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onInfo) {
                    Icon(
                        painter = painterResource(id = R.drawable.svg_percentage),
                        contentDescription = "View",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (item.impDiscount > 0) {
                    Text(
                        text = "${String.format("%.0f", item.impDiscount)}%",
                        color = Color(0xFF1976D2),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            IconButton(onClick = {
                onDelete()
                coroutineScope.launch { swipeState.animateTo(0) }
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }

            IconButton(onClick = {
                coroutineScope.launch { swipeState.animateTo(0) }
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Close Swipe",
                    tint = Color.Green
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeState.offset.value.roundToInt(), 0) }
                .swipeable(
                    state = swipeState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
                .background(Color.White)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.codigo, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text(
                item.nombre,
                modifier = Modifier.weight(2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            OutlinedTextField(
                value = qtyText,
                onValueChange = {
                    qtyText = it
                    val qty = it.toIntOrNull() ?: 0
                    onUpdate(item.copy(quantity = qty))
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = priceText,
                onValueChange = {
                    priceText = it
                    val price = it.toDoubleOrNull() ?: 0.0
                    onUpdate(item.copy(unitPrice = price))
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Text(
                "$${
                    String.format(
                        "%.2f",
                        item.quantity * item.unitPrice * (1 - item.impDiscount / 100)
                    )
                }",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumePurchaseBottomSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    addedArticles: List<ArticuloCompraDto>,
    onContinue: () -> Unit,
    uri: Uri?
) {
    if (showSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        val subtotal = addedArticles.sumOf { it.quantity * it.unitPrice }
        val totalDiscount =
            addedArticles.sumOf { it.quantity * it.unitPrice * (it.impDiscount / 100) }
        val iva = subtotal * 0.16 // mock IVA 16%
        val ieps = subtotal * 0.08 // mock IEPS 8%
        val total = subtotal - totalDiscount + iva + ieps

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                ResumeModalBottomSheet(
                    subtotal = subtotal,
                    totalDiscount = totalDiscount,
                    iva = iva,
                    ieps = ieps,
                    total = total,
                    onContinue = onContinue,
                    onDismiss = onDismiss,
                    photoUri = uri
                )
            }
        }
    }
}

