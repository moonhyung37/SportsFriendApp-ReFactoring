package com.example.sportsfriendrefac.domain.repository

import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.util.ApiResult


/*로그인, 회원가입을 처리하는 리파지토리*/
interface UserRepository {

    /* UseCase에 넣는 Repository 추상메서드  */

    //회원가입
    suspend fun registerUser(user: User): ApiResult<User>
}