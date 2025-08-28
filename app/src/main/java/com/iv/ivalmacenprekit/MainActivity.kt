package com.iv.ivalmacenprekit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.iv.ivalmacenprekit.ui.theme.IVAlmacenPrekitTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iv.ivalmacenprekit.navigation.Screen
import com.iv.ivalmacenprekit.features.auth.AuthScreen
import com.iv.ivalmacenprekit.features.home.HomeScreen
import com.iv.ivalmacenprekit.features.splash.SplashScreen
import com.iv.ivalmacenprekit.features.sucursales.SucursalesScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IVAlmacenPrekitTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route
                ) {
                    composable(Screen.Splash.route) {
                        SplashScreen(navController)
                    }
                    composable(Screen.Auth.route) {
                        AuthScreen(navController)
                    }
                    composable(Screen.Home.route) {
                        HomeScreen(navController)
                    }
                    composable(Screen.Sucursales.route) {
                        SucursalesScreen(navController)
                    }
                }
            }
        }
    }
}