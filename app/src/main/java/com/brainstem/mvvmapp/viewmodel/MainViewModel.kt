package com.brainstem.mvvmapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainstem.mvvmapp.model.Comment
import com.brainstem.mvvmapp.model.Post
import com.brainstem.mvvmapp.model.User
import com.brainstem.mvvmapp.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {
    var myPostResponse: MutableLiveData<Response<ArrayList<Post>>> = MutableLiveData()
    var myCommentResponse: MutableLiveData<Response<ArrayList<Comment>>> = MutableLiveData()
    var myUserResponse: MutableLiveData<Response<User>> = MutableLiveData()


    fun getPost(){
        viewModelScope.launch {
            myPostResponse.value = repository.getPost()
        }
    }

    fun getComment(postId: Int, sort: String, order: String = "desc"){
        viewModelScope.launch{
            myCommentResponse.value = repository.getComment(postId, sort, order)
        }
    }

    fun getUser(userId: Int){
        viewModelScope.launch{
            myUserResponse.value = repository.getUser(userId)
        }
    }

}