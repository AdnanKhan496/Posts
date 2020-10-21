package com.assignment.posts.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.assignment.posts.R
import com.assignment.posts.utils.ConnectionType
import com.assignment.posts.utils.NetworkMonitorUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class BaseActivity : AppCompatActivity() {
    private val networkMonitor = NetworkMonitorUtil(this)
    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var context : Context
    private val NETWORK_TAG = "NETWORK_MONITOR_STATUS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        setUpViews()

        val rootView = window.decorView.rootView


        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
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
                        Snackbar.make(rootView,getString(R.string.No_Connection),Snackbar.LENGTH_SHORT).show()
                        Log.i(NETWORK_TAG, getString(R.string.No_Connection))
                    }
                }
            }
        }

    }

    fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, message, duration).show()
    }

    fun setUpViews(){
        //Finding The Navigation Controller
        navController = findNavController(R.id.nav_host_fragment)

        bottomNavigationView = findViewById(R.id.bottomNavView)

        // Setting Navigation Controller with the BottomNavigationView
        bottomNavigationView.isItemHorizontalTranslationEnabled = false
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.register()

    }

    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }
}