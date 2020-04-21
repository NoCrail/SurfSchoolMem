package com.example.surfschoolmem.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalStateException

class RetrofitCallback<T>(val onSuccess: (T) -> Unit, val onError: (Throwable) -> Unit) :
    Callback<T> {
    override fun onFailure(call: Call<T>, t: Throwable) {
        onError(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        val body = response.body()

        if (response.isSuccessful && body != null) onSuccess(body)
        else onError(IllegalStateException("Unsuccessful response"))
    }

}