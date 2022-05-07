package com.example.sportsfriendrefac.util

import java.net.URLDecoder

data class ApiResult<out T>(
    val status: Status,
    val code: String?,
    val message: String?,
    val data: T?,
    val exception: Exception?,
) {
    //API 통신 상태를 확인하는 상수
    enum class Status {
        SUCCESS,
        API_ERROR,
        NETWORK_ERROR,
        LOADING
    }

    companion object {
        fun <T> success(code: String, data: T?): ApiResult<T> {
            return ApiResult(Status.SUCCESS, code, "", data, null)
        }

        //API 에러
        fun <T> error(code: String, message: String): ApiResult<T> {
            return ApiResult(Status.API_ERROR,
                URLDecoder.decode(code, "UTF-8"),
                URLDecoder.decode(message, "UTF-8"),
                null,
                null)
        }

        //네트워크 에러
        fun <T> error(exception: Exception?): ApiResult<T> {
            return ApiResult(Status.NETWORK_ERROR, null, null, null, exception)
        }

        fun <T> loading(): ApiResult<T> {
            return ApiResult(Status.LOADING, null, null, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, code=$code, message=$message, data=$data, error=$exception)"
    }


}