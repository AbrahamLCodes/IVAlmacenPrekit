package com.iv.ivalmacenprekit.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Sucursales : Screen("sucursales")
    object Purchases: Screen("purchases")
    object PurchaseArticleSelectionScreen: Screen("purchase_article_selection")
    object PurchaseDataScreen: Screen("purchase_data")
}