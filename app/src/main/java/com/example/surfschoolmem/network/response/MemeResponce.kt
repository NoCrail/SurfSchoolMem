package com.example.surfschoolmem.network.response

import com.example.surfschoolmem.structures.Meme
import com.google.gson.annotations.SerializedName

data class MemeResponce(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("isFavorite")
    val isFavorite: Boolean?,
    @SerializedName("createdDate")
    val createdDate: Long?,
    @SerializedName("photoUrl")
    val photoUrl: String?
) {
    fun convert(): Meme {
        return Meme(
            id ?: 0,
            title,
            description,
            isFavorite ?: false,
            createdDate ?: 0,
            photoUrl,
            null
        )
    }
}