package com.example.sportsfriendrefac.domain.repository

import com.example.sportsfriendrefac.domain.model.BulletinEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface BulletinRepository {

    /* UseCase에 넣는 Repository 추상메서드  */
    //모집 글 조회
    suspend fun selectBulletin(): Flow<List<BulletinEntity>>

    //내가 작성한 모집 글 조회
    suspend fun selectMyBulletin(myUserIdx: String): Flow<List<BulletinEntity>>

    //내가 작성한 모집 글 삭제
    suspend fun deleteMyBulletin(bltn_idx: String): String

    //모집 글 추가/수정
    //-추가 -> userIdx
    //-수정 -> bltnIdx
    suspend fun addEditBulletin(
        flag: Int,
        idx: String,
        title: String,
        content: String,
        imageList: List<MultipartBody.Part>,
        interestExer: String,
        address: String,
    ): BulletinEntity


}