package com.esteban.postsapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val userName: String,
    val email: String,
    val phone: String,
    val website: String,
    val company: Company
): Parcelable {
    fun getNameAndUserName(): String {
        return "$name, @$userName"
    }
}

@Parcelize
data class Company(
    val name: String,
    val catchPhrase: String,
    val bs: String
): Parcelable