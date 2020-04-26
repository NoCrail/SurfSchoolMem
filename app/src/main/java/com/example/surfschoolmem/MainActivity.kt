package com.example.surfschoolmem

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        //text.setText(pref.getString(ID_PREF, "null"))
        InitFragment()
        setSupportActionBar(toolbar)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)




    }

    fun InitFragment(){
        val fragment = FeedFragment()
        supportFragmentManager.beginTransaction().replace(R.id.FragmentContainer_Layout, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.feed -> {
                    toolbar.setTitle(getString(R.string.toolbar_popular_memes))
                    val fragment = FeedFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.FragmentContainer_Layout,
                        fragment,
                        fragment.javaClass.getSimpleName()
                    )
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.add -> {

                    val fragment = AddMemFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.FragmentContainer_Layout, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val fragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.FragmentContainer_Layout, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

}
