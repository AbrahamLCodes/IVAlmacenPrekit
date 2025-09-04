package com.iv.ivalmacenprekit.features.ComprasData

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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iv.ivalmacenprekit.features.ComprasData.data.ItemArticle
import com.iv.ivalmacenprekit.features.ComprasData.modals.PurchaseArticleSelectionModal
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PurchaseDataScreen(navController: NavController) {
    var showSheet by remember { mutableStateOf(false) }
    var selectedArticle by remember { mutableStateOf<ItemArticle?>(null) }
    var showDetailSheet by remember { mutableStateOf(false) }

    val mockRepository = remember {
        mutableStateListOf(
            ItemArticle("A001", "Articulo A", 3, 13.0),
            ItemArticle("A002", "Articulo B", 6, 8.5),
            ItemArticle(
                "A003",
                "Articulo con un nombre muy muy largo que debería truncarse",
                1,
                11.0
            ),
            ItemArticle("A004", "Articulo D", 4, 14.0)
        )
    }

    val addedMockData = remember { mutableStateListOf<ItemArticle>() }
    val total = addedMockData.sumOf { it.quantity * it.unitPrice }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
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
                    onClick = { showSheet = true },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
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

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(addedMockData, key = { it.code }) { item ->
                    SwipeableRow(
                        item = item,
                        onInfo = {
                            selectedArticle = item
                            showDetailSheet = true
                        },
                        onDelete = { addedMockData.remove(item) },
                        onUpdate = { updated ->
                            val index = addedMockData.indexOfFirst { it.code == updated.code }
                            if (index != -1) {
                                addedMockData[index] = updated
                            }
                        }
                    )
                }
            }

            Text(
                text = "Total: $${total}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )
        }

        PurchaseArticleSelectionBottomSheet(
            showSheet = showSheet,
            onDismiss = { showSheet = false },
            onAddArticle = { addedMockData.add(it) },
            onRemoveArticle = { addedMockData.remove(it) },
            repositoryData = mockRepository,
            actualData = addedMockData
        )

        if (showDetailSheet && selectedArticle != null) {
            val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { showDetailSheet = false },
                sheetState = detailSheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Detalles del Artículo", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(12.dp))
                    Text("Código: ${selectedArticle!!.code}")
                    Text("Nombre: ${selectedArticle!!.name}")
                    Text("Cantidad: ${selectedArticle!!.quantity}")
                    Text("Precio Unitario: $${selectedArticle!!.unitPrice}")
                    Text("Importe: $${selectedArticle!!.quantity * selectedArticle!!.unitPrice}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableRow(
    item: ItemArticle,
    onInfo: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: (ItemArticle) -> Unit
) {
    val swipeState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { 160.dp.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)

    var qtyText by remember(item) { mutableStateOf(item.quantity.toString()) }
    var priceText by remember(item) { mutableStateOf(item.unitPrice.toString()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onInfo) {
                Icon(Icons.Default.Info, contentDescription = "View", tint = Color.Blue)
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Close, contentDescription = "Delete", tint = Color.Red)
            }
        }

        // Foreground row
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
            Text(item.code, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text(
                item.name,
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
                "$${item.quantity * item.unitPrice}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseArticleSelectionBottomSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    onAddArticle: (ItemArticle) -> Unit,
    onRemoveArticle: (ItemArticle) -> Unit,
    repositoryData: List<ItemArticle>,
    actualData: List<ItemArticle>
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
                    repositoryData = repositoryData,
                    actualData = actualData,
                    onAddArticle = onAddArticle,
                    onRemoveArticle = onRemoveArticle
                )
            }
        }
    }
}