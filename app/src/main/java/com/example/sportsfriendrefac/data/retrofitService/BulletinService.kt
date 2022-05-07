package com.example.sportsfriendrefac.data.retrofitService

import com.example.sportsfriendrefac.data.model.Bulletin
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.*

interface BulletinService {
    //1.회원가입
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @POST("/sportsRefac/bulletin/{customUri}")
    suspend fun requestBulletin(
        @Path("customUri", encoded = true) customUri: String,
    ): Response<List<Bulletin>>
}