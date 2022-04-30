package com.example.sportsfriendrefac.di

import com.example.sportsfriendrefac.data.repository.LoginRepositoryImpl
import com.example.sportsfriendrefac.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsUserRepository(repository: LoginRepositoryImpl): LoginRepository
}
