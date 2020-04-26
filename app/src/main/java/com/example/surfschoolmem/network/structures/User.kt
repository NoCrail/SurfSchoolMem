package com.example.surfschoolmem.network.structures

data class User (
    val token: String,
    val id: Long,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val userDescription: String
)