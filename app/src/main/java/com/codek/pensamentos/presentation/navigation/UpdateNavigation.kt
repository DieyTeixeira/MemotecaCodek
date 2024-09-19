package com.codek.pensamentos.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.codek.pensamentos.data.api.ApiCreateVersionador
import com.codek.pensamentos.data.api.VersionadorApi
import com.codek.pensamentos.data.repository.VersionadorRepositoryImpl
import com.codek.pensamentos.presentation.ui.layouts.UpdateScreen
import com.codek.pensamentos.presentation.viewmodel.VersionadorViewModel

const val updateScreenRoute = "updatescreen"

fun NavGraphBuilder.updateScreen() {
    composable(updateScreenRoute) {

        val apiCreateVersionador = ApiCreateVersionador.createVersionador(VersionadorApi::class.java)
        val repositoryVersionador = VersionadorRepositoryImpl(apiCreateVersionador)
        val versionadorViewModel = VersionadorViewModel(repositoryVersionador)

        UpdateScreen(
            versionadorViewModel = versionadorViewModel
        )
    }
}

fun NavHostController.navigateToUpdateScreen() {
    navigate(updateScreenRoute)
}