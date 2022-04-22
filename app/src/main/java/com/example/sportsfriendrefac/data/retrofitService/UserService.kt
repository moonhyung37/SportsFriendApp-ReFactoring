package com.example.sportsfriendrefac.data.retrofitService

import com.example.sportsfriendrefac.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    //Restful 하게 하려면 path를 입력값에 넣어서 동적으로 변하게 만들어야 함.
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @POST("Ex_project/Ex_retrofit/example2.php")
    @FormUrlEncoded

    suspend fun registerUserApi(
        //어떤 작업을 할지는 작성되어지지 않음.
        //서버로 보내는 회원정보 데이터
        @Field("nickname") nickname: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("address") address: String,
        @Field("birthDate") birthDate: String,
    )
            : Response<User>
}