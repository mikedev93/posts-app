package com.esteban.postsapp.domain.repository

import com.esteban.postsapp.domain.model.User
import com.esteban.postsapp.domain.util.Resource

interface UsersRepository {

    suspend fun getUser(id: Int): Resource<User>
}