package com.example.surfschoolmem.utils

import android.content.Context
import com.example.surfschoolmem.R
import java.util.*

class TimeParser(private val context: Context) {
    fun parse(time: Long): String {
        val currentDate = Calendar.getInstance().timeInMillis / 1000
        val elapsed = (currentDate - time) / 60 / 60 / 24
        return when {
            elapsed < 1 -> context.getString(R.string.today)
            elapsed < 2 -> context.getString(R.string.yesterday)
            else -> context.getString(R.string.n_days_ago, elapsed)
        }
    }
}