package com.codek.pensamentos.data.api

import com.codek.pensamentos.data.model.Versionador
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface VersionadorApi {

    @GET("versionador/{id}")
    suspend fun getVersionadorById(@Path("id") id: Int): Versionador

    @PUT("versionador/{id}")
    suspend fun updateVersionador(@Path("id") id: Int, @Body versionador: Versionador): Versionador

}