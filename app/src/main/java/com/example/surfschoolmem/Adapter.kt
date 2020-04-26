package com.example.surfschoolmem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.surfschoolmem.structures.Meme

class Adapter(private val memes : List<Meme>, private val actionClicker: ActionClick) : RecyclerView.Adapter<Adapter.MemeViewHolder>() {
    inner class MemeViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val memeImage : ImageView = view.findViewById(R.id.memeImage)
        val memeTitle: TextView = view.findViewById(R.id.memeTitle)
        val favButton : ImageButton = view.findViewById(R.id.favourite_button)
        val shareButton : ImageButton = view.findViewById(R.id.share_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mem_layout, parent, false)
        return MemeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return memes.size
    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val meme = memes[position]
        holder.memeTitle.text = meme.title
        holder.memeImage.setOnClickListener{actionClicker.onMemeClick(meme)}
        if(meme.isFavorite) holder.favButton.setImageResource(R.drawable.favorite_icon)
        holder.favButton.setOnClickListener{actionClicker.onFavClick(meme, it as ImageButton, position)}
        holder.shareButton.setOnClickListener { actionClicker.onShareClick(meme) }
        Glide.with(holder.memeImage.context).load(meme.photoUrl).into(holder.memeImage)
    }

    interface ActionClick {
        fun onFavClick(meme : Meme, view: ImageButton, position: Int)
        fun onShareClick(meme : Meme)
        fun onMemeClick(meme:Meme)
    }


}