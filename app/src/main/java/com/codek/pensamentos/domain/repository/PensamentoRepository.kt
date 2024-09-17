package com.codek.pensamentos.domain.repository

import com.codek.pensamentos.data.api.PensamentoApi
import com.codek.pensamentos.data.model.Pensamento

interface PensamentoRepository {
    suspend fun getPensamentos(): List<Pensamento>
    suspend fun getPensamentoById(id: String): Pensamento
    suspend fun createPensamento(pensamento: Pensamento): Pensamento
    suspend fun deletePensamento(id: String)
    suspend fun updatePensamento(id: String, pensamento: Pensamento): Pensamento
}

class PensamentoRepositoryImpl(private val api: PensamentoApi) : PensamentoRepository {
    override suspend fun getPensamentos(): List<Pensamento> {
        return api.getPensamentos() // Método fictício, ajuste conforme a sua API
    }

    override suspend fun getPensamentoById(id: String): Pensamento {
        return api.getPensamentoById(id) // Método fictício, ajuste conforme a sua API
    }

    override suspend fun createPensamento(pensamento: Pensamento): Pensamento {
        return api.createPensamento(pensamento) // Método fictício, ajuste conforme a sua API
    }

    override suspend fun deletePensamento(id: String) {
        api.deletePensamento(id) // Método fictício, ajuste conforme a sua API
    }

    override suspend fun updatePensamento(id: String, pensamento: Pensamento): Pensamento {
        return api.updatePensamento(id, pensamento) // Método fictício, ajuste conforme a sua API
    }
}