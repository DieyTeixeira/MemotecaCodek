package com.codek.pensamentos.data.api

import com.codek.pensamentos.data.model.Pensamento
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PensamentoApi {

    @GET("pensamentos")
    suspend fun getPensamentos(): List<Pensamento>

    @GET("pensamentos/{id}")
    suspend fun getPensamentoById(@Path("id") id: String): Pensamento

    @POST("pensamentos")
    suspend fun createPensamento(@Body pensamento: Pensamento): Pensamento

    @DELETE("pensamentos/{id}")
    suspend fun deletePensamento(@Path("id") id: String)

    @PUT("pensamentos/{id}")
    suspend fun updatePensamento(@Path("id") id: String, @Body pensamento: Pensamento): Pensamento

}