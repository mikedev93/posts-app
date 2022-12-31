package com.esteban.postsapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val id: Int,
    val postId: Int,
    val name: String,
    val body: String,
    val email: String
): Parcelable
