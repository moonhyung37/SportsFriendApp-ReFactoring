package com.example.sportsfriendrefac.di

import com.example.sportsfriendrefac.domain.bulletinUseCase.BulletinSelectUseCase
import com.example.sportsfriendrefac.domain.repository.BulletinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


/* 모집 글 관련 UseCase 모음 */
@Module
@InstallIn(ViewModelComponent::class)
class BulletinUseCaseModule {
    //Presentation 계층의 ViewModel에 주입


    //모집 글 조회 유스케이스
    @Provides
    fun providesSelectBulletin(bulletinRepository: BulletinRepository):
            BulletinSelectUseCase {
        return BulletinSelectUseCase(bulletinRepository)
    }


}