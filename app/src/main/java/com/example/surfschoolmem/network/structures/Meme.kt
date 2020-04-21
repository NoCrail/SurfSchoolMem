package com.example.surfschoolmem.network.structures

data class Meme(
    val id: Long,
    val title: String?,
    val description: String?,
    val isFavorite: Boolean,
    val createdDate: Long,
    val photoUrl: String?
) {
}