package com.codek.pensamentos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codek.pensamentos.data.api.ApiClient
import com.codek.pensamentos.data.api.PensamentoApi
import com.codek.pensamentos.domain.repository.PensamentoRepositoryImpl
import com.codek.pensamentos.presentation.ui.layouts.PensamentosScreen
import com.codek.pensamentos.presentation.viewmodel.PensamentoViewModel
import com.codek.pensamentos.theme.CodekTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiClient = ApiClient.createService(PensamentoApi::class.java)
        val repository = PensamentoRepositoryImpl(apiClient)
        val viewModel = PensamentoViewModel(repository)

        setContent {
            CodekTheme {
                PensamentosScreen(
                    viewModel = viewModel
                )

            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    CodekTheme {
        PensamentosScreen(
            viewModel = PensamentoViewModel(PensamentoRepositoryImpl(ApiClient.createService(PensamentoApi::class.java)))
        )
    }
}