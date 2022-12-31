package com.esteban.postsapp.data.remote

import com.esteban.postsapp.data.remote.dto.CommentDTO
import com.esteban.postsapp.data.remote.dto.PostDTO
import com.esteban.postsapp.data.remote.dto.UserDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsAPI {

    @GET("/posts")
    suspend fun fetchPosts(@Query("_page") page: Int?, @Query("_limit") limit: Int?): Response<List<PostDTO>>

    @GET("/posts/{id}")
    suspend fun fetchPost(
        @Path("id") id: Int,
        @Query("_embed") embed: String = "comments",
        @Query("_expand") expand: String = "user"
    ): Response<PostDTO>

    @GET("/posts/{id}/comments")
    suspend fun fetchComments(@Path("id") id: Int): Response<List<CommentDTO>>
}