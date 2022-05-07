package com.example.sportsfriendrefac.domain.model

data class UserEntity(
    override var idx: String,
    override var createdDate: String,
    var profile_ImgUrl: String,
    var nickname: String,
    var email: String,
    var password: String,
    var address: String, //관심지역, 거주지역
    var birth_date: String, //상태메세지
    var content: String, //상태메세지
) : BaseData()