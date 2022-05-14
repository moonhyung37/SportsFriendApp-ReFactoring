package com.example.sportsfriendrefac.data.mapper

import com.example.sportsfriendrefac.data.model.Bulletin
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.domain.model.UserEntity


object Mapper {
    //1.회원정보조회, 수정에 사용
    //Entity -> DataModel
    fun convertUserData(userEntity: UserEntity): User.UserData {
        //userEntity null 체크
        return userEntity.run {
            User.UserData(
                userEntity.idx,
                userEntity.createdDate,
                userEntity.profile_ImgUrl,
                userEntity.nickname ?: "",
                userEntity.email,
                userEntity.password,
                userEntity.address,
            )
        }
    }

    //DataModel -> Entity
    //1.회원가입
    //2.회원정보 조회
    //3.회원정보 수정에 사용
    fun convertUserEntity(user: User.UserData): UserEntity {
        //userEntity null 체크
        return user.run {
            UserEntity(
                "",
                "",
                user.mainImgUrl ?: "",
                user.nickname,
                "",
                "",
                user.liveAddr + "@" + user.interestAddr,
                user.birthDate,
                user.content ?: "",
                user.flagFriend ?: ""
            )
        }
    }

    //2.회원가입 관련
    //Entity -> DataModel
    fun convertUserRegister(userEntity: UserEntity): User.UserRegister {
        //userEntity null 체크
        return userEntity.run {
            User.UserRegister(
                userEntity.idx,
                userEntity.createdDate,
                userEntity.profile_ImgUrl ?: "",
                userEntity.nickname ?: "",
                userEntity.email ?: "",
                userEntity.password ?: "",
                userEntity.address ?: "",
                userEntity.birth_date ?: "",
                userEntity.content ?: ""
            )
        }
    }

    //모집 글 추가, 수정, 조회
    //DataModel -> Entity
    fun convertBulletinEntity(bulletin: Bulletin): BulletinEntity {
        //userEntity null 체크
        return bulletin.run {
            BulletinEntity(
                bltn_idx,
                bltn_createdDate,
                user_idx,
                bltn_title,
                bltn_content,
                bltn_img_url ?: "",
                bltn_exer,
                bltn_addr,
                bltn_flag,
                comment_cnt ?: ""
            )
        }
    }

    //모집글 조회관련
    //Entity -> DataModel
    //data: Flow<List<Bulletin>>
    fun convertBulletinList(response: List<Bulletin>): List<BulletinEntity> {
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
                it.comment_cnt ?: "0",
            )
        }
    }

}
