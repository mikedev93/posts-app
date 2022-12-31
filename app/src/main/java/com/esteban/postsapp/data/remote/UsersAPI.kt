package com.esteban.postsapp.data.remote

import com.esteban.postsapp.data.remote.dto.UserDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersAPI {

    @GET("/users/{id}")
    suspend fun fetchUser(@Path("id") id: Int): Response<UserDTO>
}