package com.esteban.postsapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post (
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
    val user: User?,
    val comments: List<Comment>?,
    var isFavorite: Boolean = false
): Parcelable