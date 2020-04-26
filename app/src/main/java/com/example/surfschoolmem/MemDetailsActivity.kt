package com.example.surfschoolmem

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.surfschoolmem.structures.Meme
import com.example.surfschoolmem.utils.TimeParser
import kotlinx.android.synthetic.main.activity_mem_details.*

class MemDetailsActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mem_details)
        var meme = parseExtraIntent(intent)
        memeTitle.text = meme.title
        Glide.with(memeImage.context).load(meme.photoUrl).into(memeImage)
        memeDescription.text=meme.description
        favoriteButton.isChecked=meme.isFavorite
        memeDate.text = TimeParser(this).parse(meme.createdDate)

        setSupportActionBar(toolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }

    companion object{
        fun createExtraIntent(context: Context, meme: Meme): Intent {
            return Intent(context, MemDetailsActivity::class.java).apply {
                putExtra("id", meme.id)
                putExtra("title", meme.title)
                putExtra("description", meme.description)
                putExtra("isFavorite", meme.isFavorite)
                putExtra("createdDate", meme.createdDate)
                putExtra("photoUrl", meme.photoUrl)
                //putExtra("author", meme.author)
            }
        }
    }

    fun parseExtraIntent(intent: Intent): Meme =
        Meme(
            intent.getLongExtra("id", 0),
            intent.getStringExtra("title"),
            intent.getStringExtra("description"),
            intent.getBooleanExtra("isFavorite", false),
            intent.getLongExtra("createdDate", 0),
            intent.getStringExtra("photoUrl")
        )

}
