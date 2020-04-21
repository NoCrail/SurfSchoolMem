package com.example.surfschoolmem

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.surfschoolmem.network.ApiService
import com.example.surfschoolmem.network.RetrofitCallback
import com.example.surfschoolmem.network.response.MemeResponce
import com.example.surfschoolmem.network.structures.Meme
import kotlinx.android.synthetic.main.fragment_feed.*
import okhttp3.internal.notify
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment(), Adapter.ActionClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.layoutManager = StaggeredGridLayoutManager(2, 1)

        recycler.adapter = Adapter(memesList, this)

        loadMemes()
        refresh_layout.setOnRefreshListener {
            loadMemes()
            refresh_layout.isRefreshing = false
        }

    }


    fun loadMemes() {
        getMemes() {

            it.getOrNull()?.let { memes ->
                loading_error_tv.visibility = View.INVISIBLE
                memesList.clear()
                memesList.addAll(memes)
                recycler.adapter?.notifyDataSetChanged()
            }
            if (it.isFailure) {
                loading_error_tv.setText(getString(R.string.mems_loading_error_msg)) //TODO Мб можно придумать чтобы refresher натянуть на ошибку

                loading_error_tv.visibility = View.VISIBLE
            }

        }
    }

    fun getMemes(onDataReceived: (data: Result<List<Meme>>) -> Unit) {
        rt2.getMemes().enqueue(
            RetrofitCallback<List<MemeResponce>>({
                onDataReceived(Result.success(it.map { it.convert() }))
            },
                {
                    onDataReceived(Result.failure(it))
                })
        )
    }

    override fun onFavClick(meme: Meme) {
        TODO()

    }

    override fun onShareClick(meme: Meme) {
        TODO("Not yet implemented")
    }
}
