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
            favButtonSwitch(meme.isFavorite, favoriteButton)

        }

        closeMemeCreateFragment.setOnClickListener { onBackPressed() }

    }

    private fun setFavourite(meme: Meme): Meme {
        val updatedMeme = Meme(
            meme.id,
            meme.title,
            meme.description,
            !meme.isFavorite,
            meme.createdDate,
            meme.photoUrl,
            meme.author
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
                putExtra(ID_PREF, meme.id)
                putExtra(MEM_TITLE, meme.title)
                putExtra(MEM_DESCRIPTION, meme.description)
                putExtra(IS_FAVORITE, meme.isFavorite)
                putExtra(CREATED_DATE, meme.createdDate)
                putExtra(PHOTO_URL, meme.photoUrl)
                putExtra(AUTHOR_ID, meme.author)
            }
        }
    }

    private fun parseExtraIntent(intent: Intent): Meme =
        Meme(
            intent.getLongExtra(ID_PREF, 0),
            intent.getStringExtra(MEM_TITLE),
            intent.getStringExtra(MEM_DESCRIPTION),
            intent.getBooleanExtra(IS_FAVORITE, false),
            intent.getLongExtra(CREATED_DATE, 0),
            intent.getStringExtra(PHOTO_URL),
            intent.getLongExtra(AUTHOR_ID, 0).takeIf {
                it!=0L
            }
        )

}
