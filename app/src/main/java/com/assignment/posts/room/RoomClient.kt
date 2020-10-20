package com.assignment.posts.room

import androidx.room.Room
import com.assignment.posts.support.MyApplication

object RoomClient {
    //our app database object
    val appDatabase: RoomDatabase

    init {
        appDatabase = Room.databaseBuilder(MyApplication.applicationContext(), RoomDatabase::class.java, "Assignment").build()
    }
}