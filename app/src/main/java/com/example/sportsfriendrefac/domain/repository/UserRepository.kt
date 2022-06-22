package com.example.sportsfriendrefac.domain.repository

import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.util.ApiResult
import okhttp3.MultipartBody


/*로그인, 회원가입관련 데이터 로직을 처리하는 리파지토리*/
interface UserRepository {

    /* UseCase에 넣는 Repository 추상메서드  */
    //회원가입
    suspend fun registerUser(userEntity: UserEntity): ApiResult<String>

    //중복검사
    suspend fun redundancyCheck(checkData: String): ApiResult<String>

    //이메일 인증
    suspend fun certifiedEmail(email: String): ApiResult<String>

    //로그인
    suspend fun login(email: String, pw: String): ApiResult<String>

    //회원정보 조회
    suspend fun selectUserUseCase(userId: String): UserEntity

    //회원정보 수정
    suspend fun updateUserUseCase(
        userId: String,
        nickname: String,
        birthDate: String,
        address: String,
        content: String,
    ): String

    //회원정보 이미지 수정
    suspend fun updateUserImageUseCase(
        userId: String,
        imageBody: MultipartBody.Part,
    ): String

}