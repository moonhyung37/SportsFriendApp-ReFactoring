package com.example.sportsfriendrefac.data.dataSource

import com.example.sportsfriendrefac.base.BaseDataSource
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.data.retrofitService.UserService
import com.example.sportsfriendrefac.util.ApiResult
import javax.inject.Inject

/*외부 API 통신*/
abstract class LoginRemoteSource : BaseDataSource() {
    protected lateinit var keyword: String

    //회원가입
    abstract suspend fun registerUser(user: User): ApiResult<String>

    //중복검사
    abstract suspend fun redundancyCheck(checkData: String): ApiResult<String>

    //이메일 인증
    abstract suspend fun certifiedEmail(user: User): ApiResult<String>

    //로그인
    abstract suspend fun login(user: User): ApiResult<String>
}

class LoginRemoteSourceImpl @Inject constructor(
    private val userService: UserService,
) : LoginRemoteSource() {

    override suspend fun registerUser(user: User): ApiResult<String> {
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

    override suspend fun redundancyCheck(checkData: String): ApiResult<String> {
        keyword = "이메일"
        return getResponse(
            //요청 값
            userService.redundancyCheck(
                checkData,
                keyword,
                "redaduncy_check.php"
            ))
    }

    override suspend fun certifiedEmail(user: User): ApiResult<String> {
        return getResponse(
            //요청 값
            userService.certifiedEmail(
                user.email,
                "certified_email.php"
            ))
    }

    override suspend fun login(user: User): ApiResult<String> {
        return getResponse(
            //요청 값
            userService.login(
                user.email,
                user.password,
                "Login.php"
            ))
    }


}