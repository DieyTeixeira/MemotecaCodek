package com.codek.pensamentos.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.codek.pensamentos.data.api.ApiCreatePensamento
import com.codek.pensamentos.data.api.ApiCreateVersionador
import com.codek.pensamentos.data.api.PensamentoApi
import com.codek.pensamentos.data.api.VersionadorApi
import com.codek.pensamentos.data.repository.PensamentoRepositoryImpl
import com.codek.pensamentos.data.repository.VersionadorRepositoryImpl
import com.codek.pensamentos.presentation.ui.layouts.PensamentosScreen
import com.codek.pensamentos.presentation.viewmodel.PensamentoViewModel
import com.codek.pensamentos.presentation.viewmodel.VersionadorViewModel

const val pensamentosRoute = "pensamentos"

fun NavGraphBuilder.pensamentosScreen() {
    composable(pensamentosRoute) {

        val apiCreatePensamento = ApiCreatePensamento.createPensamento(PensamentoApi::class.java)
        val repositoryPensamento = PensamentoRepositoryImpl(apiCreatePensamento)
        val pensamentoViewModel = PensamentoViewModel(repositoryPensamento)
        val apiCreateVersionador = ApiCreateVersionador.createVersionador(VersionadorApi::class.java)
        val repositoryVersionador = VersionadorRepositoryImpl(apiCreateVersionador)
        val versionadorViewModel = VersionadorViewModel(repositoryVersionador)

        PensamentosScreen(
            pensamentoViewModel = pensamentoViewModel,
            versionadorViewModel = versionadorViewModel
        )
    }
}

fun NavHostController.navigateToPensamentos() {
    navigate(pensamentosRoute)
}