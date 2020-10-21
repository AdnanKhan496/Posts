package com.assignment.posts.networkLayer

import com.assignment.posts.fragments.DetailFragment
import com.assignment.posts.room.Comments
import com.assignment.posts.room.Post
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("/posts")
    fun fetchPosts() : Observable<List<Post>>

    @GET("/posts/{id}/comments")
    fun fetchComments(@Path(value = "id", encoded = false) id:String) : Observable<List<Comments>>
}