package com.iv.ivalmacenprekit.features.ComprasData.modals

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ResumeModalBottomSheet(
    subtotal: Double,
    totalDiscount: Double,
    iva: Double,
    ieps: Double,
    total: Double,
    photoUri: Uri?, // add photoUri here
    onContinue: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            "Resumen de la Compra",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        // Show the photo if available
        if (photoUri != null) {
            AsyncImage(
                model = photoUri,
                contentDescription = "Evidencia",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(16.dp))
        }

        ResumeRow("Subtotal:", subtotal)
        ResumeRow("Descuento:", totalDiscount)
        ResumeRow("IVA (16%):", iva)
        ResumeRow("IEPS (8%):", ieps)
        Divider(modifier = Modifier.padding(vertical = 12.dp))
        ResumeRow("Total:", total, bold = true)

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                onContinue()
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar")
        }
    }
}


@Composable
fun ResumeRow(label: String, amount: Double, bold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text("$${String.format("%.2f", amount)}", fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
    }
}