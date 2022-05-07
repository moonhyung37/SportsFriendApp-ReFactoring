package com.example.sportsfriendrefac.domain.repository

import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.util.ApiResult


/*로그인, 회원가입관련 데이터 로직을 처리하는 리파지토리*/
interface LoginRepository {

    /* UseCase에 넣는 Repository 추상메서드  */
    //회원가입
    suspend fun registerUser(userEntity: UserEntity): ApiResult<String>

    //중복검사
    suspend fun redundancyCheck(checkData: String): ApiResult<String>

    //이메일 인증
    suspend fun certifiedEmail(userEntity: UserEntity): ApiResult<String>

    //suspend fun certifiedEmail(userEntity: UserEntity): Flow<List<UserEntity>>
    //로그인
    suspend fun login(userEntity: UserEntity): ApiResult<String>

    //flow 반환
}