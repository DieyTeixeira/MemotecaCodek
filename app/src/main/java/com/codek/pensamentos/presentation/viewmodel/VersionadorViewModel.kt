package com.codek.pensamentos.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codek.pensamentos.data.model.Pensamento
import com.codek.pensamentos.data.model.Versionador
import com.codek.pensamentos.data.repository.VersionadorRepository
import com.codek.pensamentos.data.version.currentVersionCode
import com.codek.pensamentos.data.version.currentVersionName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VersionadorViewModel(
    private val versionadorRepository: VersionadorRepository
) : ViewModel() {

    // Variáveis para lastVersionCode e lastVersionName
    private val _lastVersionCode = MutableStateFlow<Int?>(null)
    val lastVersionCode: StateFlow<Int?> = _lastVersionCode

    private val _lastVersionName = MutableStateFlow<String?>(null)
    val lastVersionName: StateFlow<String?> = _lastVersionName

    private val _versionador = MutableStateFlow<Versionador?>(null)
    val versionador: StateFlow<Versionador?> = _versionador

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadLastVersionData(id = 1)
    }

    fun loadLastVersionData(id: Int) {
        viewModelScope.launch {
            try {
                val response = versionadorRepository.getVersionadorById(id)
                // Atualiza os valores de lastVersionCode e lastVersionName
                _lastVersionCode.value = response.lastVersionCode
                _lastVersionName.value = response.lastVersionName
                _versionador.value = response
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun updateVersionador() {
        Log.d("VersionadorViewModel", "Atualizando versionador com ID: 1")
        viewModelScope.launch {
            Log.d("VersionadorViewModel", "Tentando atualizar versionador com ID: 1")
            try {
                // Criar um novo objeto Versionador com os dados atuais
                val updatedVersionador = Versionador(
                    id = 1,
                    lastVersionCode = currentVersionCode,
                    lastVersionName = currentVersionName
                )

                val response = versionadorRepository.updateVersionador(1, updatedVersionador)
                Log.d("VersionadorViewModel", "Versionador atualizado com sucesso: $response")
                _versionador.value = response
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e("VersionadorViewModel", "Erro ao atualizar versionador", e)
                _errorMessage.value = e.message
            }
        }
    }

    fun setShowDialog(show: Boolean) {
        _showDialog.value = show
    }
}