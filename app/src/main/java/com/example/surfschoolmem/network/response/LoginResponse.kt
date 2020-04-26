package com.example.surfschoolmem.network.response

import com.example.surfschoolmem.structures.User

data class LoginResponse(val accessToken: String, val userInfo: UserInfo?) {
    fun convert(): User {
        return User (
            accessToken,
            userInfo?.id ?: -1,
            userInfo?.userName ?: "",
            userInfo?.firstName ?: "",
            userInfo?.lastName ?: "",
            userInfo?.userDescription ?: ""
        )
    }

    inner class UserInfo(
        val id: Long,
        val userName: String,
        val firstName: String,
        val lastName: String,
        val userDescription: String
    )
}