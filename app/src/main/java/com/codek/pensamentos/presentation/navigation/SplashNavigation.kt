package com.codek.pensamentos.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.codek.pensamentos.presentation.ui.layouts.SplashScreen

const val splashScreenRoute = "splashscreen"

fun NavGraphBuilder.splashScreen() {
    composable(splashScreenRoute) {

        SplashScreen()
    }
}