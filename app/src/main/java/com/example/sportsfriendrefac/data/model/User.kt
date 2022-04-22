package com.example.sportsfriendrefac.data.model

import android.content.ClipData
import android.os.Parcel
import android.os.Parcelable
import com.example.sportsfriendrefac.domain.model.Person

/*회원정보 데이터 */
data class User(
    override var idx: String,
    override var createdDate: String,
    override var profile_ImgUrl: String,
    override var nickname: String,
    var email: String,
    var password: String,
    var address: String, //관심지역, 거주이역
    var birth_date: String, //상태메세지
    var content: String, //상태메세지
) : Person() {

}

