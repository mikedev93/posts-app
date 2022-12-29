package com.esteban.postsapp.data.remote

import com.esteban.postsapp.data.remote.dto.CommentDTO
import com.esteban.postsapp.data.remote.dto.PostDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsAPI {

    @GET("/posts")
    suspend fun fetchPosts(@Query("_page") page: Int?, @Query("_limit") limit: Int = 20): Response<List<PostDTO>>

    @GET("/posts/{id}")
    suspend fun fetchPost(@Path("id") id: String): PostDTO

    @GET("/posts/{id}/comments")
    suspend fun fetchComments(@Path("id") id: String): List<CommentDTO>
}