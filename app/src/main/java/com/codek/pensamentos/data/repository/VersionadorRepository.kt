package com.codek.pensamentos.data.repository

import com.codek.pensamentos.data.api.VersionadorApi
import com.codek.pensamentos.data.model.Versionador

interface VersionadorRepository {
    suspend fun getVersionador(): List<Versionador>
    suspend fun getVersionadorById(id: Int): Versionador
    suspend fun createVersionador(versionador: Versionador): Versionador
    suspend fun deleteVersionador(id: Int)
    suspend fun updateVersionador(id: Int, versionador: Versionador): Versionador
    suspend fun getLastVersionNameById(id: Int): String
}

class VersionadorRepositoryImpl(private val api: VersionadorApi) : VersionadorRepository {
    override suspend fun getVersionador(): List<Versionador> {
        return api.getVersionador()
    }

    override suspend fun getVersionadorById(id: Int): Versionador {
        return api.getVersionadorById(id)
    }

    override suspend fun createVersionador(versionador: Versionador): Versionador {
        return api.createVersionador(versionador)
    }

    override suspend fun deleteVersionador(id: Int) {
        api.deleteVersionador(id)
    }

    override suspend fun updateVersionador(id: Int, versionador: Versionador): Versionador {
        return api.updateVersionador(1, versionador)
    }

    override suspend fun getLastVersionNameById(id: Int): String {
        return api.getVersionadorById(id).lastVersionName
    }
}