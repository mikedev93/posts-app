package com.esteban.postsapp.domain.model

import com.esteban.postsapp.domain.util.Resource

data class PaginatedResponse(
    val firstPage: Int? = null,
    val prevPage: Int? = null,
    val nextPage: Int? = null,
    val lastPage: Int? = null,
    val data: Resource<List<Post>?>?
)
