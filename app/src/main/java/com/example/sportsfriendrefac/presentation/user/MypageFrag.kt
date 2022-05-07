package com.example.sportsfriendrefac.presentation.user

import android.content.Intent
import android.view.View


import android.os.Bundle
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.databinding.FragmentMypageBinding
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.login.LoginActivity
import kotlinx.coroutines.runBlocking

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MypageFrag : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage),
    View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }


    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {

            //회원정보 수정
            binding.tvEditMypage.id -> {
                val intent = Intent(activity?.applicationContext, UserEditActivity::class.java)
                startActivity(intent)
            }
            //회원 프로필 사진 수정
            binding.liEditImgMypage.id -> {

            }

            //내가 작성한 모집글 확인
            binding.liMyBulletinMypage.id -> {

            }
            //회원탈퇴
            binding.btnDeleteMypage.id -> {

            }
            //로그아웃
            binding.btnLogoutMypage.id -> {
                runBlocking {
                    //DataStore에 저장된 회원정보 초기화(자동로그인 제거)
                    App.instance.clearDataStore()
                    //로그인 엑티비티로 이동
                    val intent = Intent(activity?.applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }

            else -> {
            }
        }
    }


    override fun init() {
        binding.tvEditMypage.setOnClickListener(this)
        binding.liEditImgMypage.setOnClickListener(this)
        binding.liMyBulletinMypage.setOnClickListener(this)
        binding.btnDeleteMypage.setOnClickListener(this)
        binding.btnLogoutMypage.setOnClickListener(this)

//        (activity as MainActivity?)?.inVisibleAllToolbarMenu()
        binding.btnDeleteMypage.visibility = View.INVISIBLE
    }


    /*   //show, Hidden 이벤트
       override fun onHiddenChanged(hidden: Boolean) {
           super.onHiddenChanged(hidden)
           //화면이 보여질 때
           if (!hidden) {
           }
       }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            //기본 생성자 생성
            MypageFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}