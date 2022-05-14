package com.example.sportsfriendrefac.data.retrofitService

import com.example.sportsfriendrefac.data.model.Bulletin
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface BulletinService {


    //1)모집글 추가
    @POST("/sportsRefac/bulletin/{customUri}")
    @Multipart
    suspend fun addBulletin(
        @Path("customUri", encoded = true) customUri: String,
        @Part("user_idx") user_idx: String,
        @Part("bltn_title") bltn_title: String,
        @Part("bltn_content") bltn_content: String,
        @Part("bltn_exer") bltn_exer: String,
        @Part("bltn_addr") bltn_addr: String,
        @Part("image_cnt") image_cnt: Int,
        @Part imageList: List<MultipartBody.Part>, //모집 글 이미지
    ): Response<Bulletin>

    //2)모집글 조회
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )

    @POST("/sportsRefac/bulletin/{customUri}")
    suspend fun selectBulletin(
        @Path("customUri", encoded = true) customUri: String,
    ): Response<List<Bulletin>>

    //3)내가 작성한 모집글 조회
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST("/sportsRefac/bulletin/{customUri}")
    suspend fun selectMyBulletin(
        @Path("customUri", encoded = true) customUri: String,
        @Field("user_idx") user_idx: String,
    ): Response<List<Bulletin>>

    //4)내가 작성한 모집글 삭제
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST("/sportsRefac/bulletin/{customUri}")
    suspend fun deleteMyBulletin(
        @Path("customUri", encoded = true) customUri: String,
        @Field("bltn_idx") bltn_idx: String,
    ): Response<String>


    //5)내가 작성한 모집글 수정
    @POST("/sportsRefac/bulletin/{customUri}")
    @Multipart
    suspend fun updateBulletin(
        @Path("customUri", encoded = true) customUri: String,
        @Part("bltn_idx") bltn_idx: String,
        @Part("bltn_title") bltn_title: String,
        @Part("bltn_content") bltn_content: String,
        @Part("bltn_exer") bltn_exer: String,
        @Part("bltn_addr") bltn_addr: String,
        @Part("image_cnt") image_cnt: Int,
        @Part imageList: List<MultipartBody.Part>, //모집 글 이미지
    ): Response<Bulletin>

}