package com.assignment.posts.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.assignment.posts.R
import com.assignment.posts.fragments.DetailFragment
import com.assignment.posts.utils.Communicator
import com.assignment.posts.utils.ConnectionType
import com.assignment.posts.utils.NetworkMonitorUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class BaseActivity : AppCompatActivity(), Communicator {
    private val networkMonitor = NetworkMonitorUtil(this)
    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var context : Context
    private val NETWORK_TAG = "NETWORK_MONITOR_STATUS"
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        setUpViews()

        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
                                //Snackbar.make(window.decorView.rootView,getString(R.string.Wifi_Connection),Snackbar.LENGTH_SHORT).show()
                                Log.i(NETWORK_TAG, getString(R.string.Wifi_Connection))
                            }
                            ConnectionType.Cellular -> {
                                Log.i(NETWORK_TAG, getString(R.string.Cellular_Connection))
                            }
                            else -> {
                            }
                        }
                    }
                    false -> {
                        Snackbar.make(window.decorView.rootView,getString(R.string.No_Connection),Snackbar.LENGTH_SHORT).show()
                        Log.i(NETWORK_TAG, getString(R.string.No_Connection))
                    }
                }
            }
        }

    }
    fun setUpViews(){
        //Finding The Navigation Controller
        navController = findNavController(R.id.nav_host_fragment)

        bottomNavigationView = findViewById(R.id.bottomNavView)

        // Setting Navigation Controller with the BottomNavigationView
        bottomNavigationView.isItemHorizontalTranslationEnabled = false
        bottomNavigationView.setupWithNavController(navController)
    }

    fun showBottomNavigation()
    {
        bottomNavigationView.visibility = View.VISIBLE
    }

    fun hideBottomNavigation()
    {
        bottomNavigationView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.register()

    }

    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }

    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id)
        {
            if (backPressedOnce)
            {
                super.onBackPressed()
                return
            }

            backPressedOnce = true
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed(2000) {
                backPressedOnce = false
            }
        }
        else {
            super.onBackPressed()
        }
    }

    //data pass between fragment
    override fun passDataCom(editTextInput: String) {
        val bundle = Bundle()
        bundle.putString("message", editTextInput)
        val transaction = this.supportFragmentManager.beginTransaction()
        val detailFragment  = DetailFragment()
        detailFragment.arguments = bundle
        transaction.replace(R.id.nav_host_fragment, detailFragment)
        transaction.commit()
    }
}