package com.esteban.postsapp.data.remote.dto

data class CommentDTO(
    val id: Int,
    val postId: Int,
    val name: String,
    val body: String,
    val email: String
)