package com.esteban.postsapp.data.remote.dto

data class PostDTO(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
    val comments: List<CommentDTO>?,
    val user: UserDTO?
)