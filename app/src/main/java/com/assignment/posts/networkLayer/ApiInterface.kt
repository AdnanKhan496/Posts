package com.assignment.posts.networkLayer

import com.assignment.posts.room.Post
import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {
    @GET("/posts")
    fun fetchPosts() : Single<List<Post>>
}