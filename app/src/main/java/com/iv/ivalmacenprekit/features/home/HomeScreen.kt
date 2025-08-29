package com.iv.ivalmacenprekit.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iv.ivalmacenprekit.R
import com.iv.ivalmacenprekit.features.shared.sweetalert.AlertType
import com.iv.ivalmacenprekit.features.shared.sweetalert.SweetAlert
import kotlinx.coroutines.delay


@Composable
fun HomeScreen(navController: NavController) {

    val menuItems = listOf(
        R.drawable.svg_document to "Prekit",
        R.drawable.svg_list to "Pedidos creados",
        R.drawable.svg_cart_new to "Compras",
        R.drawable.svg_cart_success to "Compras realizadas",
        R.drawable.svg_gears to "Configuración"
    )

    var showAlert by remember { mutableStateOf(false) }
    var alertType by remember { mutableStateOf(AlertType.INFO) }
    var alertTitle by remember { mutableStateOf("") }
    var alertMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        Box(
            modifier = Modifier.padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Red)
            )
        }

        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(menuItems.size) { index ->
                    val (icon, label) = menuItems[index]

                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(index * 150L)
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(
                            animationSpec = tween(600)
                        ) + slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        )
                    ) {
                        MenuItem(
                            icon = icon,
                            label = label,
                            onClick = {
                                if (index == 2) {
                                    alertType = AlertType.INFO
                                    alertTitle = label
                                    alertMessage = "¿Cuenta con orden de compra?"
                                    showAlert = true
                                }

                                if (index == 4) {
                                    navigateTo(navController, "auth")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAlert) {
        SweetAlert(
            type = alertType,
            title = alertTitle,
            message = alertMessage,
            confirmText = "Sí",
            cancelText = "No",
            onConfirm = { showAlert = false },
            onCancel = { showAlert = false },
            onDismiss = { showAlert = false }
        )
    }
}


@Composable
fun MenuItem(icon: Int, label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.size(72.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route)
}