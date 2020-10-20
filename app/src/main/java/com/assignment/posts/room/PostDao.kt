package com.assignment.posts.room

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface PostDao {
    @get:Query("SELECT * FROM post")
    val all: LiveData<List<Post>>

    @get:Query("SELECT * FROM post where isFavorite = 1")
    val favouritePosts: LiveData<List<Post>>

    @Insert
    suspend fun insert(post: Post?)

    @Delete
    fun delete(post: Post?)

    @Update
    suspend fun update(post: Post?)
}