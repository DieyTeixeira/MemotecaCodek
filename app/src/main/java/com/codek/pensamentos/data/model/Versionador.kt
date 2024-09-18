package com.codek.pensamentos.data.model

data class Versionador(
    val id: Int = 1,
    val lastVersion: Int,
    val lastVersionName: String,
)