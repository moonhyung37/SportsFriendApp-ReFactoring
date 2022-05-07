package com.example.sportsfriendrefac.domain.repository

import com.example.sportsfriendrefac.domain.model.BulletinEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BulletinRepository {

    /* UseCase에 넣는 Repository 추상메서드  */
    //모집 글 정보를 조회
    suspend fun selectBulletin(): Flow<List<BulletinEntity>>
}