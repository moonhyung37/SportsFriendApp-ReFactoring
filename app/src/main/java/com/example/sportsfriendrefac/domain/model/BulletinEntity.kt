package com.example.sportsfriendrefac.domain.model

data class BulletinEntity(
    //모집 글 정보
    override var idx: String, // 모집 글 인덱스 번호
    override var createdDate: String, //모집 글 작성/수정 날짜
    val user_idx: String, //작성자의 인덱스 번호
    val bltn_title: String, //모집 글 제목
    val bltn_content: String, //모집 글 내용
    val bltn_img_url: String? = null,  //모집 글 이미지
    var bltn_exer: String, //관심운동
    var bltn_addr: String, //지역
    val bltn_flag: Int, //1번 전체 모집글 2번 내가만든 모집글 (아이템뷰 분리)
    var comment_cnt: String? = null ?: "0", //모집 글 댓글 개수
) : BaseData()
