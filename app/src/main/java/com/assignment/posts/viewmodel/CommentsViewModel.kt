package com.assignment.posts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.posts.networkLayer.ApiService
import com.assignment.posts.room.Comments
import com.assignment.posts.room.Post
import com.assignment.posts.room.RoomClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CommentsViewModel: ViewModel() {
    var id : String = ""
    var poastId :String = ""

    fun getAllCommentsAsLiveData(): LiveData<List<Comments>>{
        return RoomClient.appDatabase.commentsDao()!!.allComments
    }

    fun callCommentsApi(id :String){

        ApiService.buildService().fetchComments(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { response ->
                onHandleResponse(response)
            }
    }

    fun onHandleResponse(response: List<Comments>){
        if(response.isNotEmpty()){
            response.forEach { comment ->
                viewModelScope.launch {
                    RoomClient.appDatabase.commentsDao()!!.insert(comment)
                }
            }
        }
    }

    fun addOrRemoveFavourite(comments: Comments){
        viewModelScope.launch {
            comments.isFavorite = !comments.isFavorite!!
            RoomClient.appDatabase.commentsDao()!!.update(comments)
        }
    }
}