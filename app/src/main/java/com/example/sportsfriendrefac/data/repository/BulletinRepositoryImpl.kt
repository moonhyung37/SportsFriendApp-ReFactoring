package com.example.sportsfriendrefac.data.repository

import com.example.sportsfriendrefac.data.dataSource.BulletinRemoteSource
import com.example.sportsfriendrefac.data.mapper.Mapper
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.domain.repository.BulletinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

//remoteSource 인터페이스 사용
//Di로 remoteSource의 구현체를 구현
class BulletinRepositoryImpl @Inject constructor(private val remoteSource: BulletinRemoteSource) :
    BulletinRepository {

    //모집 글 정보 조회
    override suspend fun selectBulletin(): Flow<List<BulletinEntity>> {
        //apiResult 객체반환
        return flow {
            remoteSource.requestBulletin().data?.let { Mapper.convertBulletin(it) }
                ?.let { emit(it) }
        }
    }


}