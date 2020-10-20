package com.assignment.posts.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.assignment.posts.R
import com.assignment.posts.fragments.FavouritesFragment
import com.assignment.posts.fragments.PostsFragment
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        setUpViews()

       // title=resources.getString(R.string.favourites)
       // loadFragment(PostsFragment())

       /* navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_posts-> {
                    title=resources.getString(R.string.posts)
                    loadFragment(PostsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_fav-> {
                    title=resources.getString(R.string.favourites)
                    loadFragment(FavouritesFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }*/
    }

    fun setUpViews(){
        //Finding The Navigation Controller
        navController = findNavController(R.id.fragNavHost)

        // Setting Navigation Controller with the BottomNavigationView
        bottomNavView.setupWithNavController(navController)
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}