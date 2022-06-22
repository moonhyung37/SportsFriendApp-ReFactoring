package com.example.sportsfriendrefac.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//모집 글 정보
data class Bulletin(
    @SerializedName("bltn_idx")
    @Expose
    var bltn_idx: String, // 모집 글 인덱스 번호

    @SerializedName("created_date")
    @Expose
    var bltn_createdDate: String, //모집 글 작성/수정 날짜

    @SerializedName("user_idx")
    @Expose
    val user_idx: String, //작성자의 인덱스 번호

    @SerializedName("bltn_title")
    @Expose
    val bltn_title: String, //모집 글 제목

    @SerializedName("bltn_content")
    @Expose
    val bltn_content: String, //모집 글 내용

    @SerializedName("bltn_img_url")
    @Expose
    val bltn_img_url: String, //모집 글 썸내일 이미지

    @SerializedName("bltn_exer")
    @Expose
    val bltn_exer: String, //관심운동

    @SerializedName("bltn_addr")
    @Expose
    val bltn_addr: String, //지역

    @SerializedName("bltn_flag")
    @Expose
    val bltn_flag: Int, //모집 글 상태번호 플래그

    @SerializedName("comment_cnt")
    @Expose
    val comment_cnt: String? = null ?: "0", //모집 글 댓글 개수
)