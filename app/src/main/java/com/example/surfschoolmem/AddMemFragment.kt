package com.example.surfschoolm

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.surfschoolmem.*
import com.example.surfschoolmem.database.MemesDao
import com.example.surfschoolmem.database.MemesDatabase
import com.example.surfschoolmem.dialog.SourceDialog
import com.example.surfschoolmem.structures.Meme
import kotlinx.android.synthetic.main.fragment_add_mem.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


class AddMemFragment : Fragment(), SourceDialog.ListDialogListner {

    lateinit var pref: SharedPreferences
    lateinit var dao: MemesDao
    var image:Bitmap? = null
    lateinit var foreground: Drawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_mem, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireContext().getSharedPreferences(APP_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        dao = MemesDatabase.instance(requireContext()).memesDao()
        removeImage.visibility = View.GONE
        setOnFieldsChanger()
        closeMemeCreateFragment.setOnClickListener {
            val a = activity as MainActivity
            a.showLastFragment()
        }
        removeImage.setOnClickListener {
            image=null
            removeImage.visibility = View.GONE
            Glide.with(this).clear(uploadMemeImage)
        }
        addImage.setOnClickListener {
            val dialog = SourceDialog(this)
            dialog.show((activity as AppCompatActivity).supportFragmentManager, "dialog")
        }
        createBtn.setOnClickListener {
            if(image==null) return@setOnClickListener
            val time = Calendar.getInstance().timeInMillis
            val imageFile = File(requireContext().cacheDir, "meme$time.jpg").apply {  createNewFile()}

            val bos = ByteArrayOutputStream()
            image?.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapData = bos.toByteArray()

            FileOutputStream(imageFile).apply {
                write(bitmapData)
                flush()
            }
            val updatedMeme = Meme(
                time,
                title_edit_text.text.toString(),
                description_edit_text.text.toString(),
                true,
                time/1000,
                imageFile.absolutePath,
                pref.getLong(ID_PREF, 0).takeIf {
                    it!=0L
                }
            )
            GlobalScope.launch {
                with(Dispatchers.IO) {
                    dao.insert(updatedMeme)
                }
            }
            clearFieldsAfterCreating()
        }
    }

    override fun onDialogFinish(result: SourceDialog.DialogResult) {
        when(result){
            SourceDialog.DialogResult.CAMERA -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                )
                    startCameraActivity()
                else
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 11)
            }
            SourceDialog.DialogResult.GALLERY -> {
                startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),10)
            }
        }
    }

    private fun startCameraActivity() {
        val newMemeFile = File(requireContext().cacheDir,"meme.jpg")
        val extraFile = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, newMemeFile)

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, extraFile)
        startActivityForResult(takePhotoIntent, 11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fixBitmap: Bitmap? = when {
            resultCode == Activity.RESULT_OK && data != null && requestCode == 10 -> {
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                          val source =ImageDecoder.createSource(requireContext().contentResolver,
                              data.data!!
                         )
                          ImageDecoder.decodeBitmap(source)

                } else {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data.data)
                }

            }
            resultCode == Activity.RESULT_OK && requestCode == 11 -> {
                val newFile = File(requireContext().cacheDir, "meme.jpg")

                BitmapFactory.decodeFile(newFile.path)
            }
            else -> null
        }

        image = fixBitmap
        removeImage.visibility = View.VISIBLE
        emptyMemeCheck()
        Glide.with(this).load(image).into(uploadMemeImage)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==11&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            startCameraActivity()

    }

    private fun setOnFieldsChanger(){
        foreground = createBtn.foreground
        createBtn.foreground = null
        title_edit_text.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emptyMemeCheck()
            }

        })

    }

    private fun emptyMemeCheck(){
        //context!!.theme.resolveAttribute(android.R.attr.selectableItemBackground, foreground, true)
        if((title_edit_text.length()<1)||(image==null)||(title_edit_text.length()>140)||(description_edit_text.length()>1000)){
            createBtn.isClickable = false
            createBtn.isFocusable = false
            createBtn.foreground = null
            createBtnText.setTextColor(resources.getColor(R.color.colorAccent))
        } else
        {
            createBtn.foreground = foreground
            createBtn.isClickable = true
            createBtn.isFocusable = true
            createBtnText.setTextColor(resources.getColor(R.color.ActiveColor))
        }
    }

    private fun clearFieldsAfterCreating(){
        image=null
        removeImage.visibility = View.GONE
        Glide.with(this).clear(uploadMemeImage)
        title_edit_text.text?.clear()
        description_edit_text.text?.clear()
        Toast.makeText(requireContext(), getString(R.string.meme_created_msg), Toast.LENGTH_LONG).show()
    }
}
