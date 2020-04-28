package com.example.surfschoolmem.network

import com.example.surfschoolmem.network.response.LoginResponse
import com.example.surfschoolmem.network.response.MemeResponce
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun login(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<LoginResponse>

    @GET("/memes")
    fun getMemes(): Call<List<MemeResponce>>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}