package com.example.sportsfriendrefac.domain.model

data class UserEntity(
    override var idx: String,
    override var createdDate: String,
    var profile_ImgUrl: String? = null,
    var nickname: String? = null,
    var email: String? = null,
    var password: String? = null,
    var address: String? = null, //관심지역, 거주지역
    var birth_date: String? = null, //상태메세지
    var content: String? = null, //상태메세지
    var flagFriend: String? = null, //친구여부
) : BaseData()