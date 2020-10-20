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
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        setUpViews()

    }

    fun setUpViews(){
        //Finding The Navigation Controller
        navController = findNavController(R.id.nav_host_fragment)

        bottomNavigationView = findViewById(R.id.bottomNavView)

        // Setting Navigation Controller with the BottomNavigationView
        bottomNavigationView.isItemHorizontalTranslationEnabled = false
        bottomNavigationView.setupWithNavController(navController)
    }
}