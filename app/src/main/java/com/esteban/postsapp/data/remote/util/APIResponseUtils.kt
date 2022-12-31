package com.esteban.postsapp.data.remote.util

import com.esteban.postsapp.data.remote.dto.CommentDTO
import com.esteban.postsapp.data.remote.dto.PostDTO
import com.esteban.postsapp.data.remote.dto.UserDTO
import com.esteban.postsapp.data.remote.mappers.extractURL
import com.esteban.postsapp.data.remote.mappers.toDomain
import com.esteban.postsapp.domain.model.Comment
import com.esteban.postsapp.domain.model.PaginatedResponse
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.model.User
import com.esteban.postsapp.domain.util.Resource
import retrofit2.Response

object APIResponseUtils {

    fun postsToPaginatedResponse(respose: Response<List<PostDTO>>): PaginatedResponse {
        val headers = respose.headers().toMultimap()
        val posts = respose.body() ?: mutableListOf()

        val pageInfo = headers["link"]?.first()?.split(",")?.toList()
        val pages = pageInfo?.map { it.split(";") }

        var firstPage: Int? = null
        var prevPage: Int? = null
        var nextPage: Int? = null
        var lastPage: Int? = null

        pages?.forEach { keyValuePair ->
            val uri = extractURL(keyValuePair.first())
            uri?.let {
                if (keyValuePair.last().contains("first")) {
                    firstPage = it.getQueryParameter("_page")?.toIntOrNull()
                }
                if (keyValuePair.last().contains("prev")) {
                    prevPage = it.getQueryParameter("_page")?.toIntOrNull()
                }
                if (keyValuePair.last().contains("next")) {
                    nextPage = it.getQueryParameter("_page")?.toIntOrNull()
                }
                if (keyValuePair.last().contains("last")) {
                    lastPage = it.getQueryParameter("_page")?.toIntOrNull()
                }
            }
        }

        return PaginatedResponse(
            firstPage = firstPage,
            prevPage = prevPage,
            nextPage = nextPage,
            lastPage = lastPage,
            posts = Resource.Success(
                posts.map { it.toDomain() }.toMutableList()
            )
        )
    }

    fun postResponseToPostResource(response: Response<PostDTO>): Resource<Post> {
        val post = response.body()
        return Resource.Success(post?.toDomain())
    }

    fun postUserResponseToUserResource(response: Response<UserDTO>): Resource<User> {
        val user = response.body()
        return Resource.Success(user?.toDomain())
    }

    fun postCommentsResponseToUserResource(response: Response<List<CommentDTO>>): Resource<List<Comment>> {
        val comments = response.body()
        return Resource.Success(comments?.map { it.toDomain() })
    }
}