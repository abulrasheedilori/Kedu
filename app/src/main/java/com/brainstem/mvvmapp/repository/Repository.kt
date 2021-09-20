package com.brainstem.mvvmapp.repository

import com.brainstem.mvvmapp.api.RetrofitInstance
import com.brainstem.mvvmapp.model.Comment
import com.brainstem.mvvmapp.model.Post
import com.brainstem.mvvmapp.model.User
import retrofit2.Response

class Repository {


    suspend fun getPost(): Response<ArrayList<Post>> {
        return RetrofitInstance.api.getPost()
    }
    suspend fun getComment(postId: Int, sort: String, order:String = "desc"): Response<ArrayList<Comment>> {
        return RetrofitInstance.api.getComment(postId, sort, order)
    }
    suspend fun getUser(userId:Int): Response<User>{
        return RetrofitInstance.api.getUser(userId)
    }
}