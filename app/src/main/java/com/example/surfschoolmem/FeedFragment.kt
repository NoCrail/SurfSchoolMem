package com.example.surfschoolmem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.surfschoolmem.database.MemesDao
import com.example.surfschoolmem.database.MemesDatabase
import com.example.surfschoolmem.network.ApiService
import com.example.surfschoolmem.network.RetrofitCallback
import com.example.surfschoolmem.network.response.MemeResponce
import com.example.surfschoolmem.structures.Meme
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




class FeedFragment : Fragment(), Adapter.ActionClick {

    lateinit var dao : MemesDao
    var updatedMemePosition: Int = 0

    val rt = Retrofit.Builder().apply {
        baseUrl("https://demo2407529.mockable.io/")
        addConverterFactory(GsonConverterFactory.create())
    }.build()
    val rt2 = rt.create(ApiService::class.java)

   val memesList = mutableListOf<Meme>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dao = MemesDatabase.instance(requireContext()).memesDao()

        recycler.layoutManager = StaggeredGridLayoutManager(2, 1)



        recycler.adapter = Adapter(memesList, this)
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        dao.getAll().observe(this, Observer { memes ->
            if(memes.isEmpty()){
                loading_error_tv.visibility = View.VISIBLE
                return@Observer
            }
            else loading_error_tv.visibility = View.GONE
            memesList.clear()
            memesList.addAll(memes)
            recycler.adapter?.notifyItemChanged(updatedMemePosition)

        })

        loadMemes()
        refresh_layout.setOnRefreshListener {
            loadMemes()
            refresh_layout.isRefreshing = false
        }

    }


    private fun loadMemes() {
        getMemes() {

            it.getOrNull()?.let { memes ->
                loading_error_tv.visibility = View.INVISIBLE
                //memesList.clear()
                //memesList.addAll(memes)
                GlobalScope.launch { with(Dispatchers.IO){
                    dao.insertAll(memes)

                } }

            }
            if (it.isFailure) {
                loading_error_tv.setText(getString(R.string.mems_loading_error_msg)) //TODO Мб можно придумать чтобы refresher натянуть на ошибку

                loading_error_tv.visibility = View.VISIBLE
            }

        }
    }

     private fun getMemes(onDataReceived: (data: Result<List<Meme>>) -> Unit) {
        rt2.getMemes().enqueue(
            RetrofitCallback<List<MemeResponce>>({
                onDataReceived(Result.success(it.map { it.convert() }))
            },
                {
                    onDataReceived(Result.failure(it))
                })
        )
    }

    override fun onFavClick(meme: Meme, but: ImageButton, position: Int) {
        updatedMemePosition = position
        //meme.isFavorite = !meme.isFavorite
        val updatedMeme = Meme(
            meme.id,
            meme.title,
            meme.description,
            !meme.isFavorite,
            meme.createdDate,
            meme.photoUrl
        )
        favButtonSwitch(updatedMeme.isFavorite, but)
        GlobalScope.launch { with(Dispatchers.IO){
            dao.update(updatedMeme)

        } }
    }

    override fun onShareClick(meme: Meme) {
        TODO("Not yet implemented")
    }

    override fun onMemeClick(meme: Meme) {

        startActivity(context?.let { MemDetailsActivity.createExtraIntent(it, meme) })

    }

    fun favButtonSwitch(state: Boolean, but: ImageButton){
        if(state) but.setImageResource(R.drawable.favorite_icon) else
            but.setImageResource(R.drawable.favorite_border_icon)
    }





}

