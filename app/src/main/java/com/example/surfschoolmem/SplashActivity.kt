package com.example.surfschoolmem

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    lateinit var pref: SharedPreferences
    private val SPLASH_TIME_OUT: Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        Handler().postDelayed({
            if (pref.contains(ID_PREF)) startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            ) //MainActivity
            else startActivity(Intent(this, AuthenticationActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}
