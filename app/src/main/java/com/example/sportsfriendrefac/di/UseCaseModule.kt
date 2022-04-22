package com.example.sportsfriendrefac.di

import com.example.sportsfriendrefac.domain.repository.UserRepository
import com.example.sportsfriendrefac.domain.useCase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    //회원가입 관련 유스케이스
    //Repository 인터페이스를 Repository Module에서 DI로 주입받음
    @Provides
    fun providesRegisterUseCase(userRepository: UserRepository):
            RegisterUseCase {
        //회원가입 유스케이스를 반환
        return RegisterUseCase(userRepository)
    }
}