package com.example.sportsfriendrefac.data.repository

import com.example.sportsfriendrefac.data.dataSource.BulletinRemoteSource
import com.example.sportsfriendrefac.data.mapper.Mapper
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.domain.repository.BulletinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

//remoteSource 인터페이스 사용
//Di로 remoteSource의 구현체를 구현
class BulletinRepositoryImpl @Inject constructor(private val remoteSource: BulletinRemoteSource) :
    BulletinRepository {

    //모집 글 조회
    override suspend fun selectBulletin(): Flow<List<BulletinEntity>> {
        return flow {
            remoteSource.selectBulletin().data?.let { Mapper.convertBulletinList(it) }
                ?.let { emit(it) }
        }
    }

    //내가 작성한 모집글 조회
    override suspend fun selectMyBulletin(myUserIdx: String): Flow<List<BulletinEntity>> {
        return flow {
            remoteSource.selectMyBulletin(myUserIdx).data?.let { Mapper.convertBulletinList(it) }
                ?.let { emit(it) }
        }
    }

    //내가 작성한 모집글 삭제
    override suspend fun deleteMyBulletin(bltn_idx: String): String {
        return remoteSource.deleteBulletin(bltn_idx).data ?: ""
    }

    //모집 글 추가
    override suspend fun addEditBulletin(
        flag: Int,
        idx: String,
        title: String,
        content: String,
        imageList: List<MultipartBody.Part>,
        interestExer: String,
        address: String,
    ): BulletinEntity {
        return Mapper.convertBulletinEntity(remoteSource.addEditBulletin(flag, idx,
            title,
            content,
            imageList,
            interestExer,
            address).data!!)
    }

}