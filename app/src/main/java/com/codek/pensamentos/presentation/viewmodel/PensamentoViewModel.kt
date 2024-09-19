package com.codek.pensamentos.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codek.pensamentos.data.model.Pensamento
import com.codek.pensamentos.data.repository.PensamentoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class PensamentoViewModel(
    private val pensamentoRepository: PensamentoRepository
) : ViewModel() {

    private val _pensamentos = MutableStateFlow<List<Pensamento>>(emptyList())
    val pensamentos: StateFlow<List<Pensamento>> = _pensamentos

    private val _currentPensamento = MutableStateFlow<Pensamento?>(null)
    val currentPensamento: StateFlow<Pensamento?> = _currentPensamento

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isExpandedOptions = MutableStateFlow<Int?>(null)
    val isExpandedOptions: StateFlow<Int?> = _isExpandedOptions

    private val _isExpandedCard = MutableStateFlow<Int?>(null)
    val isExpandedCard: StateFlow<Int?> = _isExpandedCard

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        loadPensamentos()
    }

    fun loadPensamentos() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = pensamentoRepository.getPensamentos()
                _pensamentos.value = response
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshPensamentos() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _isLoading.value = true
            _errorMessage.value = null
            try {
                delay(Random.nextLong(2000, 4000))
                loadPensamentos()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun deletePensamento(id: Int?) {
        if (id == null) {
            _errorMessage.value = "Pensamento inválido para deletar."
            return
        }
        viewModelScope.launch {
            try {
                pensamentoRepository.deletePensamento(id)
                _pensamentos.value = _pensamentos.value.filter { it.id != id }
                if (_currentPensamento.value?.id == id) {
                    _currentPensamento.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("PensamentoViewModel", "Erro ao deletar pensamento", e)
            }
        }
    }


    fun createPensamento(pensamento: Pensamento) {
        viewModelScope.launch {
            try {
                val newPensamento = pensamentoRepository.createPensamento(pensamento)
                _pensamentos.value = _pensamentos.value + newPensamento
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun updatePensamento(id: Int, pensamento: Pensamento) {
        viewModelScope.launch {
            try {
                val updatedPensamento = pensamentoRepository.updatePensamento(id, pensamento)
                _pensamentos.value = _pensamentos.value.map {
                    if (it.id == id) updatedPensamento else it
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun setCurrentPensamento(pensamento: Pensamento?) {
        _currentPensamento.value = pensamento
    }

    fun setShowDialog(show: Boolean) {
        _showDialog.value = show
    }

    fun setExpandedOptions(id: Int?) {
        _isExpandedOptions.value = id
    }

    fun setExpandedCard(id: Int?) {
        _isExpandedCard.value = id
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    fun setIsLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    fun setIsRefreshing(refreshing: Boolean) {
        _isRefreshing.value = refreshing
    }
}