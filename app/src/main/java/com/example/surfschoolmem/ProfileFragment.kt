package com.example.surfschoolmem

import android.content.SharedPreferences
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.surfschoolmem.database.MemesDao
import com.example.surfschoolmem.database.MemesDatabase
import com.example.surfschoolmem.structures.Meme
import kotlinx.android.synthetic.main.fragment_profile.recycler
import kotlinx.android.synthetic.main.fragment_profile.toolbar



class ProfileFragment : Fragment(), Adapter.ActionClick {

    val memesList = mutableListOf<Meme>()
    lateinit var dao: MemesDao
    var updatedMemePosition: Int = 0
    lateinit var pref: SharedPreferences

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireContext().getSharedPreferences(APP_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        recycler.layoutManager = StaggeredGridLayoutManager(2, 1)
        recycler.adapter = Adapter(memesList, this)
        recycler.itemAnimator = null
        val test = pref.getLong(ID_PREF, 0)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            toolbar.overflowIcon?.setColorFilter(BlendModeColorFilter(ContextCompat.getColor(requireContext(), R.color.ActiveColor), BlendMode.SRC_ATOP))
        } else
            toolbar.overflowIcon?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.ActiveColor), PorterDuff.Mode.SRC_ATOP)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).title=""

        dao = MemesDatabase.instance(requireContext()).memesDao()
        dao.getByAuthor(pref.getLong(ID_PREF, 0)).observe(viewLifecycleOwner, Observer { memes ->
            if (memes.isEmpty()) {
                return@Observer
            }
            memesList.clear()
            memesList.addAll(memes)
            recycler.adapter?.notifyItemChanged(updatedMemePosition)
        })



    }

    override fun onFavClick(meme: Meme, view: ImageButton, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onShareClick(meme: Meme, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onMemeClick(meme: Meme, position: Int) {
        TODO("Not yet implemented")
    }

}
