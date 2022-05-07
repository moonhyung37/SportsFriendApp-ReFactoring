package com.example.sportsfriendrefac.data.mapper

import com.example.sportsfriendrefac.data.model.Bulletin
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.domain.model.UserEntity


object Mapper {
    //Entity -> DataModel

    //로그인 회원가입 관련
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

    //모집글 조회관련
    //data: Flow<List<Bulletin>>
    fun convertBulletin(response: List<Bulletin>): List<BulletinEntity> {
        return response.toDomain()
    }

    private fun List<Bulletin>.toDomain(): List<BulletinEntity> {
        return this.map {
            BulletinEntity(
                it.bltn_idx,
                it.bltn_createdDate,
                it.user_idx,
                it.bltn_title,
                it.bltn_content,
                it.bltn_img_url,
                it.bltn_exer,
                it.bltn_addr,
                it.bltn_flag,
                it.comment_cnt,
            )
        }
    }
}
