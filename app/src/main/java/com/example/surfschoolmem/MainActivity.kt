package com.example.surfschoolmem

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.surfschoolm.AddMemFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var lastFragment = ""
    lateinit var pref: SharedPreferences
    val fragments = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragment()

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


    }

    fun initFragment() {
        val fragment = FeedFragment()
        showFragment(fragment)
        lastFragment = fragment.javaClass.simpleName
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            return@OnNavigationItemSelectedListener when (menuItem.itemId) {
                R.id.feed -> {
                    val fragment = FeedFragment()
                    showFragment(fragment)
                    lastFragment = fragment.javaClass.simpleName
                    true
                }
                R.id.add -> {

                    val fragment = AddMemFragment()
                    showFragment(fragment)
                    true
                }
                R.id.profile -> {
                    val fragment = ProfileFragment()
                    lastFragment = fragment.javaClass.simpleName
                    showFragment(fragment)
                    true
                }
                else -> false
            }

        }

    private fun showFragment(fragment: Fragment) {
        val fragmentName = fragment.javaClass.getSimpleName()

        supportFragmentManager.beginTransaction().apply {
            fragments.forEach { fragment ->
                supportFragmentManager.findFragmentByTag(fragment)?.let { hide(it) }
            }
            if (!fragments.contains(fragmentName)) {
                //Toast.makeText(applicationContext, fragmentName, Toast.LENGTH_LONG).show()
                add(R.id.FragmentContainer_Layout, fragment, fragmentName)
                fragments.add(fragmentName)
            }
            supportFragmentManager.findFragmentByTag(fragmentName)?.let { show(it) }
            commit()
        }
    }

    fun showLastFragment() {
        when (lastFragment) {
            FeedFragment::class.java.simpleName ->
                bottomNavigationView.selectedItemId = R.id.feed
            ProfileFragment::class.java.simpleName -> {
                bottomNavigationView.selectedItemId = R.id.profile
            }
        }
    }

}
