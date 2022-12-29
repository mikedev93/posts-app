package com.esteban.postsapp.data.remote.dto

import okhttp3.Headers

data class ResponseDTO(
    val headers: Headers,
    val posts: List<PostDTO>
)
