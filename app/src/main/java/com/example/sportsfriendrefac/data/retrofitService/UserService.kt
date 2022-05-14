package com.example.sportsfriendrefac.data.retrofitService

import com.example.sportsfriendrefac.data.model.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    //1.회원가입
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @POST("/sportsRefac/userjoin/{customUri}")
    @FormUrlEncoded
    suspend fun registerUserApi(
        //어떤 작업을 할지는 작성되어지지 않음.
        //서버로 보내는 회원정보 데이터
        @Field("nickname") nickname: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("address") address: String,
        @Field("birthDate") birthDate: String,
        @Path("customUri", encoded = true) customUri: String,
    ): Response<String>


    //2.이메일 인증번호 발급
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @POST("/sportsRefac/userjoin/{customUri}")
    @FormUrlEncoded
    suspend fun certifiedEmail(
        //어떤 작업을 할지는 작성되어지지 않음.
        //서버로 보내는 회원정보 데이터
        @Field("email_certified") email: String,
        @Path("customUri", encoded = true) customUri: String,
    )
            : Response<String>


    //3.중복검사
    @Headers(
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @POST("/sportsRefac/userjoin/{customUri}")
    @FormUrlEncoded
    suspend fun redundancyCheck(
        //어떤 작업을 할지는 작성되어지지 않음.
        //서버로 보내는 회원정보 데이터
        @Field("check_data") check_data: String,
        @Field("keyword") keyword: String,
        @Path("customUri", encoded = true) customUri: String,
    )
            : Response<String>

    //4.로그인
    @Headers(
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @POST("/sportsRefac/userjoin/{customUri}")
    @FormUrlEncoded
    suspend fun login(
        //어떤 작업을 할지는 작성되어지지 않음.
        //서버로 보내는 회원정보 데이터
        @Field("user_email") email: String,
        @Field("user_pw") passWord: String,
        @Path("customUri", encoded = true) customUri: String,
    )
            : Response<String>


    //회원정보 조회
    @POST("/sportsRefac/userjoin/{customUri}")
    @FormUrlEncoded
    suspend fun selectUserData(
        //어떤 작업을 할지는 작성되어지지 않음.
        //서버로 보내는 회원정보 데이터
        @Field("userId") idx: String,
        @Path("customUri", encoded = true) customUri: String,
    )
            : Response<User.UserData>

    //회원정보 수정
    @POST("/sportsRefac/userjoin/{customUri}")
    @FormUrlEncoded
    suspend fun updateUserData(
        //어떤 작업을 할지는 작성되어지지 않음.
        //서버로 보내는 회원정보 데이터
        @Field("user_idx") user_idx: String,
        @Field("user_nickname") user_nickname: String,
        @Field("user_birth_date") user_birth_date: String,
        @Field("user_addr") user_addr: String,
        @Field("user_content") user_content: String,
        @Path("customUri", encoded = true) customUri: String,
    )
            : Response<String>

    //회원 프로필 이미지 수정
    @POST("/sportsRefac/userjoin/{customUri}")
    @Multipart
    suspend fun updateUserImage(
        //어떤 작업을 할지는 작성되어지지 않음.
        //서버로 보내는 회원정보 데이터
        @Part("user_idx") user_idx: String,
        @Part file: MultipartBody.Part?, //프로필 이미지
        @Path("customUri", encoded = true) customUri: String,
    )
            : Response<String>
}