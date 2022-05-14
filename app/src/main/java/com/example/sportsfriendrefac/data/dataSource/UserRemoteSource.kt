package com.example.sportsfriendrefac.data.dataSource

import com.example.sportsfriendrefac.base.BaseDataSource
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.data.retrofitService.UserService
import com.example.sportsfriendrefac.util.ApiResult
import okhttp3.MultipartBody
import javax.inject.Inject

/*외부 API 통신*/
abstract class LoginRemoteSource : BaseDataSource() {
    protected lateinit var keyword: String

    //회원가입
    abstract suspend fun registerUser(user: User.UserRegister): ApiResult<String>

    //중복검사
    abstract suspend fun redundancyCheck(checkData: String): ApiResult<String>

    //이메일 인증
    abstract suspend fun certifiedEmail(email: String): ApiResult<String>

    //로그인
    abstract suspend fun login(email: String, pw: String): ApiResult<String>

    //회원정보 조회
    abstract suspend fun selectUserData(userId: String): ApiResult<User.UserData>

    //회원정보 수정
    abstract suspend fun updateUserData(
        userId: String,
        nickname: String,
        birthDate: String,
        address: String,
        content: String,
    ): ApiResult<String>

    //회원 프로필 이미지 수정
    abstract suspend fun updateUserImage(
        userId: String,
        imageBody: MultipartBody.Part,
    ): ApiResult<String>

}

class LoginRemoteSourceImpl @Inject constructor(
    private val userService: UserService,
) : LoginRemoteSource() {

    //회원가입
    override suspend fun registerUser(user: User.UserRegister): ApiResult<String> {
        //요청 데이터를 인자로 넣어서 서버의 응답값을 getResponse에 반환해서 받는다.
        return getResponse(userService.registerUserApi(
            //요청 값
            user.nickname,
            user.email,
            user.password,
            user.address,
            user.birth_date,
            "join.php"
        ))
    }

    //중복검사
    override suspend fun redundancyCheck(checkData: String): ApiResult<String> {
        //서버에서 어떤 중볻검사인지 확인하기 위해서
        //ex) 이메일 -> 이메일 중복검사 닉네임 -> 닉네임 중복검사
        keyword = "이메일"
        return getResponse(
            //요청 값
            userService.redundancyCheck(
                checkData,
                keyword,
                "redaduncy_check.php"
            ))
    }

    //이메일 인증번호 발급
    override suspend fun certifiedEmail(email: String): ApiResult<String> {
        return getResponse(
            //요청 값
            userService.certifiedEmail(
                //입력한 이메일
                email,
                "certified_email.php"
            ))
    }

    //로그인
    override suspend fun login(email: String, pw: String): ApiResult<String> {
        return getResponse(
            //요청 값
            userService.login(
                email, //입력한 이메일
                pw, //입력한 비밀번호
                "Login.php"
            ))
    }

    //회원정보 조회
    override suspend fun selectUserData(userId: String): ApiResult<User.UserData> {
        return getResponse(userService.selectUserData(userId, "Select_UserData.php"))
    }

    //회원정보 수정
    override suspend fun updateUserData(
        userId: String,
        nickname: String,
        birthDate: String,
        address: String,
        content: String,
    ): ApiResult<String> {
        return getResponse(userService.updateUserData(
            userId,
            nickname,
            birthDate,
            address,
            content,
            "Edit_UserData.php"))
    }

    //회원 이미지 수정
    override suspend fun updateUserImage(
        userId: String,
        imageBody: MultipartBody.Part,
    ): ApiResult<String> {
        return getResponse(userService.updateUserImage(userId, imageBody, "Edit_UserImage.php"))
    }

}