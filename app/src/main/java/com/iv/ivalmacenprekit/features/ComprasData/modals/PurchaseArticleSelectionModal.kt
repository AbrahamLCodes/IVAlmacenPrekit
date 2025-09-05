package com.iv.ivalmacenprekit.features.ComprasData.modals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iv.ivalmacenprekit.apiclient.dto.ArticuloCompraDto
import kotlin.collections.filter

@Composable
fun PurchaseArticleSelectionModal(
    onDismiss: () -> Unit,
    repositoryData: List<ArticuloCompraDto>,
    actualData: List<ArticuloCompraDto>,
    onAddArticle: (ArticuloCompraDto) -> Unit,
    onRemoveArticle: (ArticuloCompraDto) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredArticles = repositoryData.filter { it.nombre.contains(searchText, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Buscador de artículos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {

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

            LazyColumn {
                items(filteredArticles) { article ->
                    val isSelected = actualData.contains(article)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isSelected) onRemoveArticle(article) else onAddArticle(article)
                            }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = article.nombre,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )

                        if (isSelected) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "En compra",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFF44336), // red
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                IconButton(onClick = { onRemoveArticle(article) }) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Remove",
                                        tint = Color(0xFFF44336) // red
                                    )
                                }
                            }
                        } else {
                            IconButton(onClick = { onAddArticle(article) }) {
                                Icon(
                                    Icons.Default.AddCircle,
                                    contentDescription = "Add",
                                    tint = Color(0xFF4CAF50) // green
                                )
                            }
                        }
                    }

                    Divider(color = Color.LightGray.copy(alpha = 0.4f))
                }
            }
        }
    }
}
