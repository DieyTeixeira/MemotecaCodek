package com.codek.pensamentos.data.api

import com.codek.pensamentos.data.model.Pensamento
import com.codek.pensamentos.data.model.Versionador
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VersionadorApi {

    @GET("versionador")
    suspend fun getVersionador(): List<Versionador>

    @GET("versionador/{id}")
    suspend fun getVersionadorById(@Path("id") id: Int): Versionador

    @POST("versionador")
    suspend fun createVersionador(@Body versionador: Versionador): Versionador

    @DELETE("versionador/{id}")
    suspend fun deleteVersionador(@Path("id") id: Int)

    @PUT("versionador/{id}")
    suspend fun updateVersionador(@Path("id") id: Int, @Body versionador: Versionador): Versionador

}