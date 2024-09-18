package com.codek.pensamentos.data.repository

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
        return api.getPensamentos()
    }

    override suspend fun getPensamentoById(id: String): Pensamento {
        return api.getPensamentoById(id)
    }

    override suspend fun createPensamento(pensamento: Pensamento): Pensamento {
        return api.createPensamento(pensamento)
    }

    override suspend fun deletePensamento(id: String) {
        api.deletePensamento(id)
    }

    override suspend fun updatePensamento(id: String, pensamento: Pensamento): Pensamento {
        return api.updatePensamento(id, pensamento)
    }
}