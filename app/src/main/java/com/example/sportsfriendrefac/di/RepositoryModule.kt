package com.example.sportsfriendrefac.di

import com.example.sportsfriendrefac.data.repository.BulletinRepositoryImpl
import com.example.sportsfriendrefac.data.repository.UserRepositoryImpl
import com.example.sportsfriendrefac.domain.repository.BulletinRepository
import com.example.sportsfriendrefac.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    //    Domain 계층의 UseCase안에 주입
    //-UseCase안에서 Repository 인터페이스를 반환

    //로그인, 회원가입 관련 리파지토리
    @Singleton
    @Binds
    abstract fun bindsUserRepository(repository: UserRepositoryImpl): UserRepository

    //모집글 관련 리파지토리
    @Singleton
    @Binds
    abstract fun bindsBulletinRepository(repository: BulletinRepositoryImpl): BulletinRepository
}
