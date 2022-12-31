package com.esteban.postsapp.data.remote.mappers

import android.net.Uri
import com.esteban.postsapp.data.remote.dto.CommentDTO
import com.esteban.postsapp.data.remote.dto.CompanyDTO
import com.esteban.postsapp.data.remote.dto.PostDTO
import com.esteban.postsapp.data.remote.dto.UserDTO
import com.esteban.postsapp.domain.model.Comment
import com.esteban.postsapp.domain.model.Company
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.model.User

fun PostDTO.toDomain(): Post {
    return Post(
        id = this.id,
        title = this.title,
        body = this.body,
        userId = this.userId,
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
        website = this.website,
        company = this.company.toDomain()
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

fun CompanyDTO.toDomain(): Company {
    return Company(
        name = this.name,
        catchPhrase = this.catchPhrase,
        bs = this.bs
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