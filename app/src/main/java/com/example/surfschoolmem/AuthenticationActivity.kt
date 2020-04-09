package com.example.surfschoolmem

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.CalendarContract
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.android.synthetic.main.activity_authentication.view.*


class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        initEdit()
        loginButton.setOnClickListener {showProgress()
        }

    }

    private fun initEdit() {
        login_edit_text.doAfterTextChanged {
            login_field_box.error =
                if (it.isNullOrBlank()) getString(R.string.EmptyFieldMsg) else null

        }
        password_edit_text.doAfterTextChanged {
            if (it.isNullOrBlank()) password_field_box.error = getString(R.string.EmptyFieldMsg)  else
            if (it.length <= 4) {

                password_field_box.error = getString(R.string.ShortPassError)
            } else password_field_box.error = null

        }
    }

    private fun showProgress(){
        progressBar3.visibility = View.VISIBLE
        loginButton.setText("")
    }

}
