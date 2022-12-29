package com.esteban.postsapp.data.remote

import com.esteban.postsapp.data.remote.mappers.toDomain
import com.esteban.postsapp.domain.model.PaginatedResponse
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.repository.PostsRepository
import com.esteban.postsapp.domain.util.Resource
import java.lang.Exception
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    val postsAPI: PostsAPI
) : PostsRepository {

    override suspend fun getPosts(page: Int?): PaginatedResponse {
        return try {
            val response = postsAPI.fetchPosts(page)
            if (response.isSuccessful) {
                response.toDomain()
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            PaginatedResponse(
                data = Resource.Error(
                    message = "Unknown error"
                )
            )
        }
    }

    override suspend fun getPost(id: String): Resource<Post> {
        return try {
            Resource.Success(
                data = postsAPI.fetchPost(id).toDomain()
            )
        } catch (e: Exception) {
            Resource.Error (
                message = "Unknown error"
            )
        }
    }
}