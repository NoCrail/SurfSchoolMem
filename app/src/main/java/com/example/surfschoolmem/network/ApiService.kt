package com.example.surfschoolmem.network

import com.example.surfschoolmem.network.response.LoginResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun login(@Query("login") login: String, @Query("password") password: String) : Call<LoginResponse>
}