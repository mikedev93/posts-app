package com.esteban.postsapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post (
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
    var isFavorite: Boolean = false
): Parcelable {
    override fun equals(other: Any?): Boolean {
        return other is Post &&
                other.id == this.id &&
                other.title == this.title &&
                other.body == this.body &&
                other.userId == this.userId &&
                other.isFavorite == this.isFavorite
    }
}