package com.example.surfschoolmem

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.surfschoolmem.network.ApiService
import com.example.surfschoolmem.network.RetrofitCallback
import com.example.surfschoolmem.network.response.LoginResponse
import com.example.surfschoolmem.structures.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_authentication.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AuthenticationActivity : AppCompatActivity() {


    lateinit var pref: SharedPreferences

    val rt = Retrofit.Builder().apply {
        baseUrl("https://demo2407529.mockable.io/")
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    val rt2 = rt.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        initEdit()
        loginButton.setOnClickListener {
            showProgress()
            val loginval = login_edit_text.text.toString()
            val passwordval = password_edit_text.text.toString()

            login(loginval, passwordval) {
                when {
                    it.isSuccess -> {
                        val editor = pref.edit()
                        editor.putString(ID_PREF, it.getOrNull()?.id.toString())
                        editor.putString(USERNAME, it.getOrNull()?.userName)
                        editor.putString(FIRST_NAME, it.getOrNull()?.firstName)
                        editor.putString(LAST_NAME, it.getOrNull()?.lastName)
                        editor.putString(USER_DESCRIPTION, it.getOrNull()?.userDescription)
                        editor.apply()
                        startActivity(Intent(this, MainActivity::class.java)) //MainActivity
                        finish()
                    }
                    else -> {

                        hideProgress()
                        val snackbar = Snackbar.make(
                            linearlayout,
                            getString(R.string.login_error_msg),
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.view.setBackgroundColor(getColor(R.color.ErrorColor))
                        snackbar.show()


                    }
                }
            }
        }


    }

    fun login(login: String, password: String, onDataReceived: (data: Result<User>) -> Unit) {
        rt2.login(login, password).enqueue(
            RetrofitCallback<LoginResponse>({

                onDataReceived(Result.success(it.convert()))
            },
                {

                    onDataReceived(Result.failure(it))
                })
        )


    }

    private fun initEdit() {
        login_edit_text.doAfterTextChanged {
            login_field_box.error =
                if (it.isNullOrBlank()) getString(R.string.empty_field_msg) else null

        }
        password_edit_text.doAfterTextChanged {
            if (it.isNullOrBlank()) password_field_box.error =
                getString(R.string.empty_field_msg) else
                if (it.length <= 4) {

                    password_field_box.error = getString(R.string.short_password_msg)
                } else password_field_box.error = null

        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
        loginButton.setText("")
    }

    private fun hideProgress() {
        progressBar.visibility = View.INVISIBLE
        loginButton.setText(getString(R.string.login_lbl))

    }


}
