package com.example.sportsfriendrefac.base

import com.example.sportsfriendrefac.util.ApiResult
import com.example.sportsfriendrefac.util.Constants
import retrofit2.Response

//리파지토리를 상속함으로 Retrofit Api 호출에 대한 보일러플레이트 코드를 줄일 수 있음.
open class BaseDataSource {
    suspend fun <T> getResponse(response: Response<T>): ApiResult<T> {
        return try {
            //성공
            if (response.isSuccessful) {
                return ApiResult.success(Constants.SUCCESS_CODE, response.body())
            }
            //실패
            else {
                val code = response.headers()[Constants.Fail_CODE] ?: ""
                val message = response.headers()[Constants.FAIL_MSG] ?: ""
                ApiResult.error(code, message)
            }
        } catch (e: Exception) {
            ApiResult.error(e)
        }
    }
}