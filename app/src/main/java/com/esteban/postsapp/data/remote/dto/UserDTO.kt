package com.esteban.postsapp.data.remote.dto

data class UserDTO(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val company: CompanyDTO
)

data class CompanyDTO(
    val name: String,
    val catchPhrase: String,
    val bs: String
)