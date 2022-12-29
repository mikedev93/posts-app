package com.esteban.postsapp.domain.model

data class User(
    val id: Int,
    val name: String,
    val userName: String,
    val email: String,
    val phone: String,
    val website: String
)