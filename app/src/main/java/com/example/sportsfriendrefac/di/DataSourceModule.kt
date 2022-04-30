package com.example.sportsfriendrefac.di

import com.example.sportsfriendrefac.data.dataSource.LoginRemoteSource
import com.example.sportsfriendrefac.data.dataSource.LoginRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    //RemoteSource 인터페이스를 구현한 구현체
    abstract fun bindUserRemoteService(source: LoginRemoteSourceImpl):
            LoginRemoteSource
}