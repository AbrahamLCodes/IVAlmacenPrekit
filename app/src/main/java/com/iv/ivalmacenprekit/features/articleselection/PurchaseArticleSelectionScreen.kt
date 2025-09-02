package com.iv.ivalmacenprekit.features.articleselection

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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iv.ivalmacenprekit.apiclient.dto.DataItemDto

@Composable
fun PurchaseArticleSelectionScreen(navController: NavController) {

    // Mock data
    val allArticles = remember {
        mutableStateListOf(
            DataItemDto(1, "Manzana"),
            DataItemDto(2, "Banana"),
            DataItemDto(3, "Naranja"),
            DataItemDto(4, "Pera"),
            DataItemDto(5, "Uva"),
            DataItemDto(6, "Fresa"),
            DataItemDto(7, "Mango"),
            DataItemDto(8, "Piña"),
            DataItemDto(9, "Sandía"),
            DataItemDto(10, "Melón"),
            DataItemDto(11, "Kiwi"),
            DataItemDto(12, "Cereza"),
            DataItemDto(13, "Durazno"),
            DataItemDto(14, "Limón"),
            DataItemDto(15, "Mandarina"),
            DataItemDto(16, "Frambuesa"),
            DataItemDto(17, "Arándano"),
            DataItemDto(18, "Papaya"),
            DataItemDto(19, "Guayaba"),
            DataItemDto(20, "Higo")
        )
    }

    var searchText by remember { mutableStateOf("") }
    val selectedArticles = remember { mutableStateListOf<DataItemDto>() }

    val filteredArticles = allArticles.filter {
        it.descripcion.contains(searchText, ignoreCase = true)
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
                    text = "Buscador de artículos",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = { /* TODO navigate next  */ },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
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
            // Search input
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar artículo") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // List of articles
            LazyColumn {
                items(filteredArticles) { article ->
                    val isSelected = selectedArticles.contains(article)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation =
                            CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = article.descripcion,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            if (isSelected) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "En compra",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    IconButton(onClick = { selectedArticles.remove(article) }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Remove")
                                    }
                                }
                            } else {
                                IconButton(onClick = { selectedArticles.add(article) }) {
                                    Icon(Icons.Default.AddCircle, contentDescription = "Add")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}