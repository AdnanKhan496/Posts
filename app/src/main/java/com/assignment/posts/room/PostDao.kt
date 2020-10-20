package com.assignment.posts.room

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface PostDao {
    @get:Query("SELECT * FROM post")
    val all: LiveData<List<Post>>

    @get:Query("SELECT * FROM post")
    val dataSize: List<Post>

    @Insert
    fun insert(task: Post?)

    @Delete
    fun delete(task: Post?)

    @Update
    fun update(task: Post?)
}