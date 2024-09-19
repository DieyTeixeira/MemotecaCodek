package com.codek.pensamentos

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.codek.pensamentos.data.api.ApiCreatePensamento
import com.codek.pensamentos.data.api.ApiCreateVersionador
import com.codek.pensamentos.data.api.PensamentoApi
import com.codek.pensamentos.data.api.VersionadorApi
import com.codek.pensamentos.data.repository.PensamentoRepositoryImpl
import com.codek.pensamentos.data.repository.VersionadorRepositoryImpl
import com.codek.pensamentos.data.version.currentVersionName
import com.codek.pensamentos.presentation.navigation.navigateToPensamentos
import com.codek.pensamentos.presentation.navigation.navigateToUpdateScreen
import com.codek.pensamentos.presentation.navigation.pensamentosScreen
import com.codek.pensamentos.presentation.navigation.splashScreen
import com.codek.pensamentos.presentation.navigation.splashScreenRoute
import com.codek.pensamentos.presentation.navigation.updateScreen
import com.codek.pensamentos.presentation.ui.layouts.PensamentosScreen
import com.codek.pensamentos.presentation.viewmodel.PensamentoViewModel
import com.codek.pensamentos.presentation.viewmodel.VersionadorViewModel
import com.codek.pensamentos.theme.CodekTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CodekTheme {

                val navController = rememberNavController()
                val apiCreateVersionador = ApiCreateVersionador.createVersionador(VersionadorApi::class.java)
                val repositoryVersionador = VersionadorRepositoryImpl(apiCreateVersionador)
                val versionadorViewModel = VersionadorViewModel(repositoryVersionador)

                LaunchedEffect(Unit) {
                    versionadorViewModel.lastVersionName.collect { lastVersionName ->
                        lastVersionName?.let {
                            checkForUpdate(it) { isUpdateVersion, isUpdateApp, isVersionOk ->
                                when {
                                    isUpdateVersion -> {
                                        versionadorViewModel.updateVersionador()
                                        navController.navigateToPensamentos()
                                        Log.d("MainActivity", "Entrou aqui na atualização do versionador")
                                    }
                                    isUpdateApp -> {
                                        navController.navigateToUpdateScreen()
                                        requestPermissions(this@MainActivity)
                                        Log.d("MainActivity", "Entrou aqui na atualização do app")
                                    }
                                    isVersionOk -> {
                                        navController.navigateToPensamentos()
                                        Log.d("MainActivity", "Entrou aqui na versão ok")
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
        val currentVersionName = currentVersionName
        val isUpdateVersion = currentVersionName > lastVersionName
        val isUpdateApp = currentVersionName < lastVersionName
        val isVersionOk = currentVersionName == lastVersionName
        onResult(isUpdateVersion, isUpdateApp, isVersionOk)
    }

    fun requestPermissions(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.REQUEST_INSTALL_PACKAGES
            )

            // Verificar se as permissões já foram concedidas
            if (permissions.any { context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }) {
                // Solicitar permissões
                (context as Activity).requestPermissions(permissions, REQUEST_CODE)
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 100
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing\n      in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and\n      handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Todas as permissões foram concedidas
                Toast.makeText(this, "Permissões concedidas", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "As permissões são necessárias para instalar a atualização", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    CodekTheme {
            val pensamentoViewModel = PensamentoViewModel(
                PensamentoRepositoryImpl(
                    ApiCreatePensamento.createPensamento(
                        PensamentoApi::class.java
                    )
                )
            )
            val versionadorViewModel = VersionadorViewModel(
                VersionadorRepositoryImpl(
                    ApiCreateVersionador.createVersionador(
                        VersionadorApi::class.java
                    )
                )
            )

        PensamentosScreen(
            pensamentoViewModel = pensamentoViewModel,
            versionadorViewModel = versionadorViewModel
        )
    }
}