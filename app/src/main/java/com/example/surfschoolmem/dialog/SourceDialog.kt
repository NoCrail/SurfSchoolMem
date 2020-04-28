package com.example.surfschoolmem.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import com.example.surfschoolmem.R

class SourceDialog(private val target: ListDialogListner): DialogFragment() {
    interface ListDialogListner{
        fun onDialogFinish(result: DialogResult)
    }

    enum class DialogResult{
        GALLERY, CAMERA
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_source, container, false)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        v.findViewById<ListView>(R.id.sourceList).setOnItemClickListener { _, _, position,_  ->
            val result = when (position) {
                0 -> DialogResult.GALLERY
                else -> DialogResult.CAMERA
            }
            target.onDialogFinish(result)

            dialog?.dismiss()
        }

        return v
    }
}