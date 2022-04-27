package com.example.sportsfriendrefac.data.repository

import com.example.sportsfriendrefac.data.dataSource.LoginRemoteSource
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.domain.repository.LoginRepository
import com.example.sportsfriendrefac.util.ApiResult
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val remoteSource: LoginRemoteSource) :
    LoginRepository {
    /* 내부 DB(Room, Realm)통신을 하고 싶으면 추가적으로 인터페이스를 정의해도됨 */
    //회원가입
    override suspend fun registerUser(user: User): ApiResult<String> {
        return remoteSource.registerUser(user)
    }

    //중복검사
    override suspend fun redundancyCheck(checkData: String): ApiResult<String> {
        return remoteSource.redundancyCheck(checkData)
    }

    //이메일 인증
    override suspend fun certifiedEmail(user: User): ApiResult<String> {
        return remoteSource.certifiedEmail(user)
    }

    //로그인
    override suspend fun login(user: User): ApiResult<String> {
        return remoteSource.login(user)
    }
}
