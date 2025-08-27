package com.iv.ivalmacenprekit.features.shared.customtoast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning

enum class ToastType { SUCCESS, WARNING, INFO, DANGER }

@Composable
fun AppToast(
    message: String,
    type: ToastType,
    visible: Boolean,
    onDismiss: () -> Unit,
    duration: Long = 2000L // ms
) {
    var show by remember { mutableStateOf(visible) }

    // Auto-dismiss after duration
    LaunchedEffect(visible) {
        if (visible) {
            show = true
            delay(duration)
            show = false
            onDismiss()
        }
    }

    // Colors + Icons
    val (bgColor, icon, iconTint) = when (type) {
        ToastType.SUCCESS -> Triple(Color(0xFF4CAF50), Icons.Default.CheckCircle, Color.White)
        ToastType.WARNING -> Triple(Color(0xFFFFC107), Icons.Default.Warning, Color.Black)
        ToastType.INFO -> Triple(Color(0xFF2196F3), Icons.Default.Info, Color.White)
        ToastType.DANGER -> Triple(Color(0xFFF44336), Icons.Default.Close, Color.White)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = show,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .background(bgColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(icon, contentDescription = null, tint = iconTint)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
