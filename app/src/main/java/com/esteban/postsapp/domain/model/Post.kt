package com.esteban.postsapp.domain.model

data class Post (
    val id: Int,
    val title: String,
    val body: String,
    val user: User?,
    val comments: List<Comment>?,
    var isFavorite: Boolean = false
)