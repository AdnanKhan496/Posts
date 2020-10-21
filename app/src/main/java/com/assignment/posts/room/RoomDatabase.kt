package com.assignment.posts.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Post::class, Comments::class], version = 1)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao?
    abstract  fun commentsDao(): CommentsDao?
}