package com.assignment.posts.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.posts.networkLayer.ApiService
import com.assignment.posts.room.Post
import com.assignment.posts.room.RoomClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*


class PostViewModel: ViewModel() {

    fun getAllPostAsLiveData(): LiveData<List<Post>>{
        return RoomClient.appDatabase.postDao()!!.all
    }

    fun getFavouriteAsLiveData(): LiveData<List<Post>>{
        return RoomClient.appDatabase.postDao()!!.favouritePosts
    }

    fun callPostsApi(){

        ApiService.buildService().fetchPosts()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { response ->
                onHandleResponse(response)
            }
    }

    fun onHandleResponse(response: List<Post>){
        if(response.isNotEmpty()){
            response.forEach { post ->
                viewModelScope.launch {
                    RoomClient.appDatabase.postDao()!!.insert(post)
                }
            }
        }
    }

    fun addOrRemoveFavourite(post: Post){
        viewModelScope.launch {
            post.isFavorite = !post.isFavorite!!
            RoomClient.appDatabase.postDao()!!.update(post)
        }
    }
}