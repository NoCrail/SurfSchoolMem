package com.example.surfschoolmem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.surfschoolmem.database.MemesDao
import com.example.surfschoolmem.database.MemesDatabase
import com.example.surfschoolmem.structures.Meme
import com.example.surfschoolmem.utils.TimeParser
import kotlinx.android.synthetic.main.activity_mem_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MemDetailsActivity : AppCompatActivity() {

    lateinit var dao: MemesDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mem_details)
        var meme = parseExtraIntent(intent)
        memeTitle.text = meme.title
        Glide.with(memeImage.context).load(meme.photoUrl).into(memeImage)
        memeDescription.text = meme.description
        favButtonSwitch(meme.isFavorite, favoriteButton)
        memeDate.text = TimeParser(this).parse(meme.createdDate)

        dao = MemesDatabase.instance(applicationContext).memesDao()
        favoriteButton.setOnClickListener {
            meme = setFavourite(meme)
            favButtonSwitch(meme.isFavorite, it as ImageButton)

        }


        setSupportActionBar(toolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun setFavourite(meme: Meme): Meme {
        val updatedMeme = Meme(
            meme.id,
            meme.title,
            meme.description,
            !meme.isFavorite,
            meme.createdDate,
            meme.photoUrl
        )
        GlobalScope.launch {
            with(Dispatchers.IO) {
                dao.update(updatedMeme)
            }
        }
        return updatedMeme
    }

    private fun favButtonSwitch(state: Boolean, but: ImageButton) {
        if (state) but.setImageResource(R.drawable.favorite_icon)
        else but.setImageResource(R.drawable.favorite_border_icon)
    }

    companion object {
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

    private fun parseExtraIntent(intent: Intent): Meme =
        Meme(
            intent.getLongExtra("id", 0),
            intent.getStringExtra("title"),
            intent.getStringExtra("description"),
            intent.getBooleanExtra("isFavorite", false),
            intent.getLongExtra("createdDate", 0),
            intent.getStringExtra("photoUrl")
        )

}
