package com.assignment.posts.support

import android.app.Application

class MyApplication: Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        private var instance: Application? = null

        @JvmStatic
        fun applicationContext() : MyApplication {
            return instance as MyApplication
        }
    }
}