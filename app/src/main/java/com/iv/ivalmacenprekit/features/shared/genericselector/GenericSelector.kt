package com.iv.ivalmacenprekit.features.shared.genericselector

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> GenericSelector(
    items: List<T>,
    selectedItem: T?,
    itemId: (T) -> Any,
    itemText: (T) -> String,
    label: String,
    onItemSelected: (T) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 250.dp)
                .clip(RoundedCornerShape(5.dp))
                .border(0.8.dp, Color.LightGray, RoundedCornerShape(5.dp))
                .padding(15.dp)
        ) {
            items(items) { item ->
                val isSelected = selectedItem?.let { itemId(it) == itemId(item) } ?: false

                Text(
                    text = itemText(item),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemSelected(item) }
                        .padding(12.dp)
                        .background(
                            if (isSelected) Color.LightGray.copy(alpha = 0.5f)
                            else Color.Transparent
                        )
                )
            }
        }

        Text(
            text = label,
            fontSize = 17.sp,
            color = Color.Gray,
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 4.dp)
                .align(Alignment.TopStart)
                .offset(x = 0.dp, y = (-10).dp)
        )
    }
}