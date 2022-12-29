package com.esteban.postsapp.domain.model

data class Comment(
    val id: Int,
    val postId: Int,
    val name: String,
    val body: String,
    val email: String
)
