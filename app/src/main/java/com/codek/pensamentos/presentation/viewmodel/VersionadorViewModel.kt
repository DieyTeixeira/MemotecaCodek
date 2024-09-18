package com.codek.pensamentos.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codek.pensamentos.data.model.Versionador
import com.codek.pensamentos.data.repository.VersionadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VersionadorViewModel(
    private val versionadorRepository: VersionadorRepository
) : ViewModel() {

    private val _lastVersionName = MutableStateFlow<String?>(null)
    val lastVersionName: StateFlow<String?> = _lastVersionName

    private val _showVersionDialog = MutableStateFlow(false)
    val showVersionDialog: StateFlow<Boolean> = _showVersionDialog

    private val _versionMessage = MutableStateFlow("")
    val versionMessage: StateFlow<String> = _versionMessage

    fun loadLastVersionNameById(id: Int) {
        viewModelScope.launch {
            try {
                val versionName = versionadorRepository.getLastVersionNameById(id)
                _lastVersionName.value = versionName
            } catch (e: Exception) {
                _lastVersionName.value = "Erro ao conectar com o banco de versionamento"
            }
        }
    }

    fun updateVersionador(newVersionador: Versionador) {
        viewModelScope.launch {
            try {
                versionadorRepository.updateVersionador(newVersionador.id, newVersionador)
                setVersionMessage("Versão atualizada com sucesso no servidor!")
            } catch (e: Exception) {
                setVersionMessage("Erro ao atualizar a versão: ${e.message}")
            } finally {
                setShowVersionDialog(true)
            }
        }
    }

    fun setVersionMessage(message: String) {
        _versionMessage.value = message
    }

    fun setShowVersionDialog(show: Boolean) {
        _showVersionDialog.value = show
    }
}