package com.example.sportsfriendrefac.data.mapper

import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.domain.model.UserEntity


object Mapper {
    //Entity -> User
    fun convertUser(userEntity: UserEntity): User {
        //userEntity null 체크
        return userEntity.run {
            User(
                userEntity.idx,
                userEntity.createdDate,
                userEntity.profile_ImgUrl,
                userEntity.nickname,
                userEntity.email,
                userEntity.password,
                userEntity.address,
                userEntity.birth_date,
                userEntity.content,
            )
        }

    }
}
