package com.example.surfschoolmem

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.surfschoolmem.database.MemesDao
import com.example.surfschoolmem.database.MemesDatabase
import com.example.surfschoolmem.network.ApiService
import com.example.surfschoolmem.structures.Meme
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProfileFragment : Fragment(), Adapter.ActionClick {

    val memesList = mutableListOf<Meme>()
    lateinit var dao: MemesDao
    var updatedMemePosition: Int = 0
    lateinit var pref: SharedPreferences
    val rt = Retrofit.Builder().apply {
        baseUrl("https://demo2407529.mockable.io/")
        addConverterFactory(GsonConverterFactory.create())
    }.build()
    val apiService = rt.create(ApiService::class.java)

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
        pref = requireContext()
            .getSharedPreferences(APP_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        recycler.layoutManager = StaggeredGridLayoutManager(2, 1)
        recycler.adapter = Adapter(memesList, this)
        recycler.itemAnimator = null
        userName.text = pref.getString(FIRST_NAME, "")
        userDesc.text = pref.getString(USER_DESCRIPTION, "")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            toolbar.overflowIcon?.setColorFilter(
                BlendModeColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.ActiveColor
                    ), BlendMode.SRC_ATOP
                )
            )
        } else
            toolbar.overflowIcon?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.ActiveColor
                ), PorterDuff.Mode.SRC_ATOP
            )
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).title = ""

        dao = MemesDatabase.instance(requireContext()).memesDao()

        dao.getByAuthor(pref.getLong(ID_PREF, 0)).observe(viewLifecycleOwner, Observer { memes ->
            if (memes.isEmpty()) {
                return@Observer
            }
            memesList.clear()
            memesList.addAll(memes)
            if (memesList.size > 1) recycler.adapter?.notifyDataSetChanged()
            else
                recycler.adapter?.notifyItemChanged(updatedMemePosition)
        })


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logoutMenuBtn) {
            val dialog =
                AlertDialog.Builder(requireContext()).apply {
                    setTitle(getString(R.string.logout_dialog_title))
                    setMessage("")
                    setPositiveButton(getString(R.string.logout_dialog_logout)) { _: DialogInterface, _: Int ->
                        GlobalScope.launch {
                            with(Dispatchers.IO) {
                                val logout = apiService.logout()
                                if (logout.isSuccessful) {
                                    pref.edit().clear().apply()
                                    activity?.finish()
                                    startActivity(
                                        Intent(
                                            context,
                                            AuthenticationActivity::class.java
                                        )
                                    )
                                } else
                                    Toast.makeText(
                                        context,
                                        getString(R.string.mems_loading_error_msg),
                                        Toast.LENGTH_LONG
                                    ).show()
                            }
                        }

                    }
                    setNegativeButton(getString(R.string.logout_dialog_cancel), null)
                }.create()

            dialog.setOnShowListener {
                dialog
                    .getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getColor(requireContext(), R.color.ActiveColor))
                dialog
                    .getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getColor(requireContext(), R.color.ActiveColor))
            }
            dialog.show()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onFavClick(meme: Meme, but: ImageButton, position: Int) {
        updatedMemePosition = position
        val updatedMeme = Meme(
            meme.id,
            meme.title,
            meme.description,
            !meme.isFavorite,
            meme.createdDate,
            meme.photoUrl,
            meme.author
        )
        favButtonSwitch(updatedMeme.isFavorite, but)
        GlobalScope.launch {
            with(Dispatchers.IO) {
                dao.update(updatedMeme)

            }
        }
    }

    override fun onShareClick(meme: Meme, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onMemeClick(meme: Meme, position: Int) {
        updatedMemePosition = position
        startActivity(context?.let { MemDetailsActivity.createExtraIntent(it, meme) })
        recycler.adapter?.notifyItemChanged(updatedMemePosition)
    }

    private fun favButtonSwitch(state: Boolean, but: ImageButton) {
        if (state) but.setImageResource(R.drawable.favorite_icon) else
            but.setImageResource(R.drawable.favorite_border_icon)
    }

}
