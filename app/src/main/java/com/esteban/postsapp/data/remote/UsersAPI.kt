package com.esteban.postsapp.data.remote

import com.esteban.postsapp.data.remote.dto.UserDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersAPI {

    @GET("/users/{id}")
    suspend fun fetchUser(@Query("id") id: String): UserDTO
}