package com.esteban.postsapp.di

import com.esteban.postsapp.data.remote.PostsRepositoryImpl
import com.esteban.postsapp.data.remote.UsersRepositoryImpl
import com.esteban.postsapp.domain.repository.PostsRepository
import com.esteban.postsapp.domain.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPostsRepository(
        postsRepositoryImpl: PostsRepositoryImpl
    ): PostsRepository

    @Binds
    @Singleton
    abstract fun bindUsersRepository(
        usersRepositoryImpl: UsersRepositoryImpl
    ): UsersRepository
}