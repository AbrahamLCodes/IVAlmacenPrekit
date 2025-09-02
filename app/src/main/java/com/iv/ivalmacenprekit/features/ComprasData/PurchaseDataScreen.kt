package com.iv.ivalmacenprekit.features.ComprasData

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iv.ivalmacenprekit.navigation.Screen

/*
*
* Vamos a crear el Text fecha y hora

* Luego mostrar 5 Texts como si fueran cabeceros para una lista tipo table:
Codigo, Articulo, Cantidad, Costo Unitario, Importe
* Luego desplegamos una lista scrolleable en donde mostremos data que haga match con estos cabeceros
* En el pie de la pagina agregamos un Text llamado "Total" en donde se muestre el total de los articulos x su cantidad
* Luego Abajo agregamos 2 botones llamado Evidencia Factura y Guardar en el mismo Row con weights iguales
* */
@Composable
fun PurchaseDataScreen(navController: NavController) {

    // Mock data para la lista
    val articles = remember {
        mutableStateListOf(
            Article("A001", "Manzana", 5, 10.0),
            Article("A002", "Banana", 3, 7.5),
            Article("A003", "Naranja", 2, 12.0),
            Article("A004", "Pera", 8, 9.0)
        )
    }

    val total = articles.sumOf { it.quantity * it.unitPrice }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {

        // TopBar
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

                IconButton(
                    onClick = { navController.navigate(Screen.PurchaseArticleSelectionScreen.route) },
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
                .padding(16.dp)
        ) {
            Text(
                text = "Fecha: ${
                    java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                }",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Codigo", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Articulo", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Text("Cantidad", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Costo Unitario", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Importe", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color.Gray, thickness = 1.dp)

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(articles) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.code, modifier = Modifier.weight(1f))
                        Text(item.name, modifier = Modifier.weight(2f))
                        Text(item.quantity.toString(), modifier = Modifier.weight(1f))
                        Text("$${item.unitPrice}", modifier = Modifier.weight(1f))
                        Text("$${item.quantity * item.unitPrice}", modifier = Modifier.weight(1f))
                    }
                    Divider(color = Color.LightGray)
                }
            }

            Text(
                text = "Total: $${total}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* TODO Evidencia Factura */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Evidencia Factura")
                }

                Button(
                    onClick = { /* TODO Guardar */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

// Clase de datos
data class Article(
    val code: String,
    val name: String,
    val quantity: Int,
    val unitPrice: Double
)