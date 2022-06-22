package com.example.sportsfriendrefac.di

import com.example.sportsfriendrefac.domain.repository.UserRepository
import com.example.sportsfriendrefac.domain.userUseCase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
/*로그인, 회원가입 관련 유스케이스 */
class UserUseCaseModule {
    //회원가입 관련 유스케이스
    //Repository 인터페이스를 Repository Module에서 DI로 주입받음
    @Provides
    fun providesRegisterUseCase(userRepository: UserRepository):
            RegisterUseCase {
        //회원가입 유스케이스를 반환
        return RegisterUseCase(userRepository)
    }

    //이메일 인증 유스케이스
    @Provides
    fun providesCertifiedEmail(userRepository: UserRepository):
            CertifiedEmailUseCase {
        return CertifiedEmailUseCase(userRepository)
    }

    //중복검사 유스케이스
    @Provides
    fun providesRedundancy(userRepository: UserRepository):
            RedundancyUseCase {
        return RedundancyUseCase(userRepository)
    }

    //로그인 유스케이스
    @Provides
    fun providesLogin(userRepository: UserRepository):
            LoginUseCase {
        return LoginUseCase(userRepository)
    }


    //회원정보 조회 유스케이스
    @Provides
    fun providesSelectUser(userRepository: UserRepository):
            SelectUserUseCase {
        return SelectUserUseCase(userRepository)
    }

    //회원정보 수정 유스케이스
    @Provides
    fun providesUpdateUser(userRepository: UserRepository):
            UpdateUserUseCase {
        return UpdateUserUseCase(userRepository)
    }

    //회원 이미지 수정 유스케이스
    @Provides
    fun providesUpdateUserImage(userRepository: UserRepository):
            UpdateUserImageUseCase {
        return UpdateUserImageUseCase(userRepository)
    }
}