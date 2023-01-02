package com.esteban.postsapp.data.remote

import com.esteban.postsapp.data.remote.util.APIResponseUtils
import com.esteban.postsapp.domain.model.Comment
import com.esteban.postsapp.domain.model.PaginatedResponse
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.repository.PostsRepository
import com.esteban.postsapp.domain.util.Resource
import com.google.gson.JsonElement
import java.io.IOException
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    val postsAPI: PostsAPI
) : PostsRepository {

    override suspend fun getPosts(page: Int?, limit: Int?): PaginatedResponse {
        return try {
            val response = postsAPI.fetchPosts(page, limit)
            if (!response.isSuccessful) throw IOException("Response error")

            APIResponseUtils.postsToPaginatedResponse(response)
        } catch (e: Exception) {
            PaginatedResponse(
                posts = Resource.Error(
                    message = "Unknown error"
                )
            )
        }
    }

    override suspend fun getPost(id: Int): Resource<Post> {
        return try {
            val response = postsAPI.fetchPost(id)
            if (!response.isSuccessful) throw IOException("Response error")

            APIResponseUtils.postResponseToPostResource(response)
        } catch (e: Exception) {
            Resource.Error (
                message = "Unknown error"
            )
        }
    }

    override suspend fun deletePost(id: Int): Resource<JsonElement> {
        return try {
            val response = postsAPI.deletePost(id)
            if (!response.isSuccessful) throw IOException("Response error")

            Resource.Success(
                data = null
            )
        } catch (e: Exception) {
            Resource.Error (
                message = "Unknown error"
            )
        }
    }

    override suspend fun getComments(id: Int): Resource<List<Comment>> {
        return try {
            val response = postsAPI.fetchComments(id)
            if (!response.isSuccessful) throw IOException("Response error")

            APIResponseUtils.postCommentsResponseToUserResource(response)
        } catch (e: Exception) {
            Resource.Error (
                message = "Unknown error"
            )
        }
    }
}