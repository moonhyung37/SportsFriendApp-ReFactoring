package com.example.sportsfriendrefac.data.repository

import com.example.sportsfriendrefac.data.dataSource.LoginRemoteSource
import com.example.sportsfriendrefac.data.mapper.Mapper
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.domain.repository.UserRepository
import com.example.sportsfriendrefac.util.ApiResult
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val remoteSource: LoginRemoteSource) :
    UserRepository {

    /* 모든 Data 계층의 모델은 UserEntity로 변환되어서 ViewModel에 전달됨. */

    //회원가입
    override suspend fun registerUser(userEntity: UserEntity): ApiResult<String> {
        return remoteSource.registerUser(Mapper.convertUserRegister(userEntity))
    }

    //중복검사
    override suspend fun redundancyCheck(checkData: String): ApiResult<String> {
        return remoteSource.redundancyCheck(checkData)
    }

    //이메일 인증
    override suspend fun certifiedEmail(email: String): ApiResult<String> {
        return remoteSource.certifiedEmail(email)
    }


    //로그인
    override suspend fun login(email: String, pw: String): ApiResult<String> {
        //mapper로 변환
        return remoteSource.login(email, pw)
    }

    //회원정보 조회
    //시행착오를 거치면서 ViewModel에는 Api의 최종적인 응답값을 반환해야 한다고생각해서
    //ApiResult -> UserEntity로 변경함
    override suspend fun selectUserUseCase(userId: String): UserEntity {
        return remoteSource.selectUserData(userId).data!!.let { Mapper.convertUserEntity(it) }
    }

    //회원정보 수정
    override suspend fun updateUserUseCase(
        userId: String,
        nickname: String,
        birthDate: String,
        address: String,
        content: String,
    ): String {
        return remoteSource.updateUserData(userId,
            nickname,
            birthDate,
            address,
            content).data.toString()
    }

    //회원 이미지 수정
    override suspend fun updateUserImageUseCase(
        userId: String,
        imageBody: MultipartBody.Part,
    ): String {
        return remoteSource.updateUserImage(userId, imageBody).toString()
    }


}
