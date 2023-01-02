package com.esteban.postsapp.data.remote

import com.esteban.postsapp.data.remote.dto.CommentDTO
import com.esteban.postsapp.data.remote.dto.PostDTO
import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsAPI {

    @GET("/posts")
    suspend fun fetchPosts(@Query("_page") page: Int?, @Query("_limit") limit: Int?): Response<List<PostDTO>>

    @GET("/posts/{id}")
    suspend fun fetchPost(
        @Path("id") id: Int
    ): Response<PostDTO>

    @DELETE("/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Int
    ): Response<JsonElement>

    @GET("/posts/{id}/comments")
    suspend fun fetchComments(@Path("id") id: Int): Response<List<CommentDTO>>
}