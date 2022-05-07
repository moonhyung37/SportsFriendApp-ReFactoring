package com.example.sportsfriendrefac.util

import android.annotation.SuppressLint
import com.example.sportsfriendrefac.R
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationBarView


object Constants {

    //API 호출 관련
    const val SUCCESS_CODE = "200"
    const val Fail_CODE = "400"

    //    const val SUCCESS_MSG = "Success"
    const val FAIL_MSG = "Failed"

    //DataStore 키
    const val USERIDKEY = "USERIDKEY"
    const val USERNICKNAMEKEY = "USERNICKNAMEKEY"
    const val USERPROFILEIMGKEY = "USERPROFILEIMGKEY"

    const val SELECTKEY = "SELECTKEY"

    //기본 이미지 경로
    const val Base_img_url: String = "http://3.37.253.243/sports_friend/app_image/"
//    const val Base_img_url: String = "http://3.37.253.243/sportsRefac/app_image/"

    //모집 글 기본 이미지
    val bltn_default_image = R.drawable.bltn_default_img

}