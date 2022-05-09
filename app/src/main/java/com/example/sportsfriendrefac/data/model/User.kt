package com.example.sportsfriendrefac.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*회원정보 데이터 */


sealed class User {
    //회원정보 조회에 사용
    data class UserSelect(
        @SerializedName("user_nickname")
        @Expose
        var nickname: String, //닉네임
        @SerializedName("user_birth_date")
        @Expose
        var birthDate: String, //생년월일
        @SerializedName("user_img_url")
        @Expose
        var mainImgUrl: String? = null, //프로필 사진
        @SerializedName("live_addr")
        @Expose
        var liveAddr: String, //거주지역
        @SerializedName("interest_addr")
        @Expose
        var interestAddr: String? = null, //관심지역
        @SerializedName("user_content")
        @Expose
        var content: String? = null, //상태메세지
        @SerializedName("friend_flag")
        @Expose
        var flagFriend: String? = null,  //친구여부
    )

    //회원가입에 사용
    //응답값으로 String을 받으므로 따로 SealizedName을 설정하지 않음.
    data class UserRegister(
        var idx: String,
        var createdDate: String,
        var profile_ImgUrl: String,
        var nickname: String,
        var email: String,
        var password: String,
        var address: String, //관심지역, 거주이역
        var birth_date: String, //상태메세지
        var content: String, //상태메세지
    )
}


