package com.assignment.posts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.assignment.posts.networkLayer.ApiService
import com.assignment.posts.room.Post
import com.assignment.posts.room.RoomClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PostViewModel: ViewModel(), CoroutineScope {

    private var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    init {
        job = Job()
    }

    fun getAllPostAsLiveData(): LiveData<List<Post>>{
        return RoomClient.appDatabase.postDao()!!.all
    }

    fun postSize(): Int{
        return RoomClient.appDatabase.postDao()!!.dataSize.size
    }

    fun callPostsApi(){

        ApiService.buildService().fetchPosts()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { response ->
                launch {
                    onHandleResponse(response)
                }
            }
    }

    suspend fun onHandleResponse(response: List<Post>){
        if(response.size != 0){
            response.forEach { post ->
                withContext(Dispatchers.IO){
                    RoomClient.appDatabase.postDao()!!.insert(post)
                }
            }
        }
    }

    fun markAsFavourit(post: Post){
        launch {
            post.isFavorite = true
            withContext(Dispatchers.IO) {
                RoomClient.appDatabase.postDao()!!.update(post)
            }
        }
    }
}