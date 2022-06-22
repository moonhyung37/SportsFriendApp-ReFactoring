package com.example.sportsfriendrefac.di

import com.example.sportsfriendrefac.data.dataSource.BulletinRemoteSource
import com.example.sportsfriendrefac.data.dataSource.BulletinRemoteSourceImpl
import com.example.sportsfriendrefac.data.dataSource.LoginRemoteSource
import com.example.sportsfriendrefac.data.dataSource.LoginRemoteSourceImpl
import com.example.sportsfriendrefac.data.repository.BulletinRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    //인터페이스의 구현체를 입력값으로 받아서 DataSource객체를 반환
    /* Retrofit을 이용한 서버 통신에 사용 */
    @Singleton
    @Binds
    //로그인, 회원가입 관련 DataSource
    abstract fun bindUserRemoteService(remoteSource: LoginRemoteSourceImpl):
            LoginRemoteSource

    @Singleton
    @Binds
    //모집글 관련 DataSource
    abstract fun bindBulletinRemoteService(remoteSource: BulletinRemoteSourceImpl):
            BulletinRemoteSource
}