package com.example.sportsfriendrefac.di

import com.example.sportsfriendrefac.domain.repository.LoginRepository
import com.example.sportsfriendrefac.domain.loginUseCase.CertifiedEmailUseCase
import com.example.sportsfriendrefac.domain.loginUseCase.LoginUseCase
import com.example.sportsfriendrefac.domain.loginUseCase.RedundancyUseCase
import com.example.sportsfriendrefac.domain.loginUseCase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
/*로그인, 회원가입 관련 유스케이스 */
class LoginUseCaseModule {
    //회원가입 관련 유스케이스
    //Repository 인터페이스를 Repository Module에서 DI로 주입받음
    @Provides
    fun providesRegisterUseCase(loginRepository: LoginRepository):
            RegisterUseCase {
        //회원가입 유스케이스를 반환
        return RegisterUseCase(loginRepository)
    }

    //이메일 인증 유스케이스
    @Provides
    fun providesCertifiedEmail(loginRepository: LoginRepository):
            CertifiedEmailUseCase {
        return CertifiedEmailUseCase(loginRepository)
    }

    //중복검사 유스케이스
    @Provides
    fun providesRedundancy(loginRepository: LoginRepository):
            RedundancyUseCase {
        return RedundancyUseCase(loginRepository)
    }

    //로그인 유스케이스
    @Provides
    fun providesLogin(loginRepository: LoginRepository):
            LoginUseCase {
        return LoginUseCase(loginRepository)
    }
}