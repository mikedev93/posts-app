package com.esteban.postsapp.data.remote.mappers

import android.net.Uri
import com.esteban.postsapp.data.remote.dto.CommentDTO
import com.esteban.postsapp.data.remote.dto.PostDTO
import com.esteban.postsapp.data.remote.dto.UserDTO
import com.esteban.postsapp.domain.model.Comment
import com.esteban.postsapp.domain.model.PaginatedResponse
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.model.User
import com.esteban.postsapp.domain.util.Resource
import retrofit2.Response

fun Response<List<PostDTO>>.toDomain(): PaginatedResponse {
    val headers = this.headers().toMultimap()
    val posts = this.body()

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
        data = Resource.Success(posts?.map { it.toDomain() })
    )
}

fun PostDTO.toDomain(): Post {
    return Post(
        id = this.id,
        title = this.title,
        body = this.body,
        user = this.user?.toDomain(),
        comments = this.comments?.map { it.toDomain() }
    )
}

fun UserDTO.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        userName = this.username,
        email = this.email,
        phone = this.phone,
        website = this.website
    )
}

fun CommentDTO.toDomain(): Comment {
    return Comment(
        id = this.id,
        postId = this.postId,
        name = this.name,
        body = this.body,
        email = this.email
    )
}

fun extractURL(text: String): Uri? {
    val regex = "[<>]"
    return try {
        Uri.parse(text.replace(regex.toRegex(), ""))
    } catch (e: Exception) {
        null
    }
}