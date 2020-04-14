package com.example.surfschoolmem

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.surfschoolmem.network.ApiService
import com.example.surfschoolmem.network.RetrofitCallback
import com.example.surfschoolmem.network.response.LoginResponse
import com.example.surfschoolmem.network.structures.User
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    val APP_PREFERENCES = "user_setup"

    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        text.setText(pref.getString(ID_PREF, "null"))






    }



}
