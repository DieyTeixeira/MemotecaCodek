package com.codek.pensamentos.data.repository

import com.codek.pensamentos.data.api.VersionadorApi
import com.codek.pensamentos.data.model.Versionador

interface VersionadorRepository {
    suspend fun getVersionadorById(id: Int): Versionador
    suspend fun updateVersionador(id: Int, versionador: Versionador): Versionador
}

class VersionadorRepositoryImpl(private val api: VersionadorApi) : VersionadorRepository {

    override suspend fun getVersionadorById(id: Int): Versionador {
        return api.getVersionadorById(id)
    }

    override suspend fun updateVersionador(id: Int, versionador: Versionador): Versionador {
        return api.updateVersionador(id, versionador)
    }
}