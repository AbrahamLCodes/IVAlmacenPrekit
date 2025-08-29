package com.iv.ivalmacenprekit.features.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iv.ivalmacenprekit.R
import com.iv.ivalmacenprekit.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {

    var redirectDone by remember { mutableStateOf(false) }

    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }

    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(2000)
        if (!redirectDone) {
            redirectDone = true
            if (viewModel.isLoggedIn) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            } else {
                navController.navigate(Screen.Auth.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = LinearOutSlowInEasing
            )
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = {
                    overshootEasing(it)
                }
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(800)
        textAlpha.animateTo(1f, animationSpec = tween(800))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.iv_logo_cropped),
            contentDescription = "Logo",
            modifier = Modifier
                .weight(2f)
                .padding(40.dp)
                .graphicsLayer(
                    alpha = alpha.value,
                    scaleX = scale.value,
                    scaleY = scale.value
                )
        )

        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "V2 R1",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(textAlpha.value)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}


fun overshootEasing(t: Float): Float {
    val tension = 2f
    val inner = t - 1.0f
    return inner * inner * ((tension + 1) * inner + tension) + 1.0f
}
