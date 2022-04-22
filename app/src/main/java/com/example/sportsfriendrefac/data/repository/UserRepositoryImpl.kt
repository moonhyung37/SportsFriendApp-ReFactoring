package com.example.sportsfriendrefac.data.repository

import com.example.sportsfriendrefac.data.dataSource.RemoteSource
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.domain.repository.UserRepository
import com.example.sportsfriendrefac.util.ApiResult
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val remoteSource: RemoteSource) :
    UserRepository {
    /* 내부 DB(Room, Realm)통신을 하고 싶으면 추가적으로 인터페이스를 정의해도됨 */
    override suspend fun registerUser(user: User):ApiResult<User> {
        return remoteSource.registerUser(user)
    }
}
