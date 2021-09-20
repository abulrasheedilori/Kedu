package com.brainstem.mvvmapp.api

import com.brainstem.mvvmapp.model.Comment
import com.brainstem.mvvmapp.model.Post
import com.brainstem.mvvmapp.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetApi {
    @GET("posts")
    suspend fun getPost(): Response<ArrayList<Post>>

    @GET("comments")
    suspend fun getComment(@Query("postId") postId: Int,
                           @Query("_sort") sort: String,
                           @Query("_order") order: String
    ): Response<ArrayList<Comment>>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): Response<User>
}