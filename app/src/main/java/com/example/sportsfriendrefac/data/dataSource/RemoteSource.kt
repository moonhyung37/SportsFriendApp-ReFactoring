package com.example.sportsfriendrefac.data.dataSource

import com.example.sportsfriendrefac.base.BaseDataSource
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.data.retrofitService.UserService
import com.example.sportsfriendrefac.util.ApiResult
import javax.inject.Inject

/*외부 API 통신*/
abstract class RemoteSource : BaseDataSource() {
    abstract suspend fun registerUser(user: User): ApiResult<User>
}

class RemoteSourceImpl @Inject constructor(
    private val userService: UserService,
) : RemoteSource() {

    override suspend fun registerUser(user: User): ApiResult<User> {
        return getResponse(userService.registerUserApi(
            //회원정보를 입력 받음
            user.nickname,
            user.email,
            user.password,
            user.address,
            user.birth_date,
        ))
    }
}