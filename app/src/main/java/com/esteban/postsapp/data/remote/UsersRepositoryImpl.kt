package com.esteban.postsapp.data.remote

import com.esteban.postsapp.data.remote.util.APIResponseUtils
import com.esteban.postsapp.domain.model.User
import com.esteban.postsapp.domain.repository.UsersRepository
import com.esteban.postsapp.domain.util.Resource
import java.io.IOException
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    val usersAPI: UsersAPI
): UsersRepository{

    override suspend fun getUser(id: Int): Resource<User> {
        return try {
            val response = usersAPI.fetchUser(id)
            if (!response.isSuccessful) throw IOException("Response error")

            APIResponseUtils.postUserResponseToUserResource(response)
        } catch (e: Exception) {
            Resource.Error("Unknown error")
        }
    }
}