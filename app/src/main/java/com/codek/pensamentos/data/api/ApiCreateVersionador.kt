package com.codek.pensamentos.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiCreateVersionador {
    private const val BASE_URL = "https://codekst.com.br/"

    fun createVersionador(apiClass: Class<VersionadorApi>): VersionadorApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(apiClass)
    }
}