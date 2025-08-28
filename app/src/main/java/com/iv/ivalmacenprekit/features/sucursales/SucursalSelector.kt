package com.iv.ivalmacenprekit.features.sucursales

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iv.ivalmacenprekit.apiclient.dto.SucursalDto
import androidx.compose.foundation.lazy.items

@Composable
fun SucursalSelector(
    sucursales: List<SucursalDto>,
    selectedSucursal: SucursalDto?,
    onSucursalSelected: (SucursalDto) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(15.dp)
        ) {
            LazyColumn {
                items(sucursales) { sucursal ->
                    val isSelected = selectedSucursal?.idSucursal == sucursal.idSucursal
                    Text(
                        text = sucursal.nombre,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSucursalSelected(sucursal)
                            }
                            .padding(12.dp)
                            .background(
                                if (isSelected) Color.LightGray.copy(alpha = 0.5f)
                                else Color.Transparent
                            )
                    )
                }
            }
        }

        Text(
            text = "Sucursal",
            fontSize = 17.sp,
            color = Color.Black,
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 4.dp)
                .align(Alignment.TopStart)
                .offset(x = 12.dp, y = (-10).dp)
        )
    }
}