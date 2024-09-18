package com.codek.pensamentos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.codek.pensamentos.data.api.ApiCreatePensamento
import com.codek.pensamentos.data.api.ApiCreateVersionador
import com.codek.pensamentos.data.api.PensamentoApi
import com.codek.pensamentos.data.api.VersionadorApi
import com.codek.pensamentos.data.repository.PensamentoRepositoryImpl
import com.codek.pensamentos.data.repository.VersionadorRepositoryImpl
import com.codek.pensamentos.presentation.ui.layouts.PensamentosScreen
import com.codek.pensamentos.presentation.viewmodel.PensamentoViewModel
import com.codek.pensamentos.presentation.viewmodel.VersionadorViewModel
import com.codek.pensamentos.theme.CodekTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.codek.pensamentos.data.model.Versionador
import com.codek.pensamentos.presentation.navigation.UpdateScreen
import com.codek.pensamentos.presentation.navigation.navigateToPensamentos
import com.codek.pensamentos.presentation.navigation.navigateToUpdateScreen
import com.codek.pensamentos.presentation.navigation.pensamentosScreen
import com.codek.pensamentos.presentation.navigation.splashScreenRoute
import com.codek.pensamentos.presentation.navigation.splashScreen
import com.codek.pensamentos.presentation.navigation.updateScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CodekTheme {

                val navController = rememberNavController()

                // instanciando o viewModel do versionador
                val apiCreateVersionador = ApiCreateVersionador.createVersionador(VersionadorApi::class.java)
                val repositoryVersionador = VersionadorRepositoryImpl(apiCreateVersionador)
                val versionadorViewModel = VersionadorViewModel(repositoryVersionador)

                LaunchedEffect(Unit) {
                    versionadorViewModel.loadLastVersionNameById(1)
                    versionadorViewModel.lastVersionName.collect { lastVersionName ->
                        lastVersionName?.let {
                            checkForUpdate(it) { isUpdateVersion, isUpdateApp, isVersionOk ->
                                versionadorViewModel.setShowVersionDialog(true)
                                when {
                                    isUpdateVersion -> {
                                        versionadorViewModel.setVersionMessage("App está à frente da versão mais recente.")
                                        val currentVersion = Versionador(
                                            id = 1,
                                            lastVersion = BuildConfig.VERSION_CODE,
                                            lastVersionName = BuildConfig.VERSION_NAME
                                        )
                                        versionadorViewModel.updateVersionador(currentVersion)
                                        navController.navigateToPensamentos()
                                    }
                                    isUpdateApp -> {
                                        versionadorViewModel.setVersionMessage("App está desatualizado!")
                                        navController.navigateToUpdateScreen()
                                    }
                                    isVersionOk -> {
                                        navController.navigateToPensamentos()
                                        versionadorViewModel.setVersionMessage("App está atualizado corretamente.")
                                    }
                                }
                            }
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = splashScreenRoute
                ) {
                    splashScreen()
                    updateScreen()
                    pensamentosScreen()
                }
            }
        }
    }

    private fun checkForUpdate(
        lastVersionName: String,
        onResult: (isUpdateVersion: Boolean, isUpdateApp: Boolean, isVersionOk: Boolean) -> Unit
    ) {
        val currentVersionName = BuildConfig.VERSION_NAME
        val isUpdateVersion = currentVersionName > lastVersionName
        val isUpdateApp = currentVersionName < lastVersionName
        val isVersionOk = currentVersionName == lastVersionName
        onResult(isUpdateVersion, isUpdateApp, isVersionOk)
    }
}

@Preview
@Composable
private fun AppPreview() {
    CodekTheme {
        PensamentosScreen(
            pensamentoViewModel = PensamentoViewModel(
                PensamentoRepositoryImpl(
                    ApiCreatePensamento.createPensamento(
                        PensamentoApi::class.java
                    )
                )
            ),
            versionadorViewModel = VersionadorViewModel(
                VersionadorRepositoryImpl(
                    ApiCreateVersionador.createVersionador(
                        VersionadorApi::class.java
                    )
                )
            )
        )
    }
}