package com.codek.pensamentos.data.repository

import com.codek.pensamentos.data.api.PensamentoApi
import com.codek.pensamentos.data.model.Pensamento

interface PensamentoRepository {
    suspend fun getPensamentos(): List<Pensamento>
    suspend fun getPensamentoById(id: Int): Pensamento
    suspend fun createPensamento(pensamento: Pensamento): Pensamento
    suspend fun deletePensamento(id: Int)
    suspend fun updatePensamento(id: Int, pensamento: Pensamento): Pensamento
}

class PensamentoRepositoryImpl(private val api: PensamentoApi) : PensamentoRepository {
    override suspend fun getPensamentos(): List<Pensamento> {
        return api.getPensamentos()
    }

    override suspend fun getPensamentoById(id: Int): Pensamento {
        return api.getPensamentoById(id)
    }

    override suspend fun createPensamento(pensamento: Pensamento): Pensamento {
        return api.createPensamento(pensamento)
    }

    override suspend fun deletePensamento(id: Int) {
        api.deletePensamento(id)
    }

    override suspend fun updatePensamento(id: Int, pensamento: Pensamento): Pensamento {
        return api.updatePensamento(id, pensamento)
    }
}