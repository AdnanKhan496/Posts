package com.assignment.posts.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CommentsDao {
    @get:Query("SELECT * FROM comments")
    val allComments: LiveData<List<Comments>>


    @Insert
    suspend fun insert(comments: Comments?)

    @Delete
    fun delete(comments: Comments?)

    @Update
    suspend fun update(comments: Comments?)
}