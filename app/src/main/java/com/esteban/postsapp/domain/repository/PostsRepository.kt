package com.esteban.postsapp.domain.repository

import com.esteban.postsapp.domain.model.PaginatedResponse
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.util.Resource

interface PostsRepository {

    suspend fun getPosts(page: Int? = null): PaginatedResponse

    suspend fun getPost(id: String): Resource<Post>
}