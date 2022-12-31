package com.esteban.postsapp.domain.repository

import com.esteban.postsapp.domain.model.Comment
import com.esteban.postsapp.domain.model.PaginatedResponse
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.util.Resource

interface PostsRepository {

    suspend fun getPosts(page: Int? = null, limit: Int? = null): PaginatedResponse

    suspend fun getPost(id: Int): Resource<Post>

    suspend fun getComments(id: Int): Resource<List<Comment>>
}