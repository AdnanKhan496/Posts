package com.assignment.posts.networkLayer

import com.assignment.posts.room.Post
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiInterface {
    @GET("/posts")
    fun fetchPosts() : Observable<List<Post>>
}