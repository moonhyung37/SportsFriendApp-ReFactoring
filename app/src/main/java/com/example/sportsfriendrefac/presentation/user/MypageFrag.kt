package com.example.sportsfriendrefac.presentation.user

import android.content.Intent
import android.view.View


import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.databinding.FragmentMypageBinding
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.extension.repeatOnStarted
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.login.LoginActivity
import com.example.sportsfriendrefac.presentation.viewModel.MainViewModel
import com.example.sportsfriendrefac.util.Constants
import kotlinx.coroutines.runBlocking

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MypageFrag : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage),
    View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: MainViewModel by activityViewModels<MainViewModel>()

    var userIdx: String? = null
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

        binding.btnDeleteMypage.visibility = View.INVISIBLE
        //DataStore에 저장된 회원정보 idx
        userIdx = (activity as MainActivity?)?.userIdx
        //회원정보 서버에 요청
        userIdx?.run { viewModel.selectUserData(this) }

        //ViewModel에서 받은 이벤트를 받음.
        repeatOnStarted {
            viewModel.sharedUser.collect { event ->
                handleEvent(event)
            }
        }


    }

    //viewModel에서 전달한 Event객체에 따라서 값을 처리하는 메서드
    private fun handleEvent(eventUser: MainViewModel.EventUser) = when (eventUser) {

        is MainViewModel.EventUser.UserData -> setUserDataUi(eventUser.userEntity)
    }


    //회원정보 UI에 입력하는 메서드ㄹ
    private fun setUserDataUi(userEntity: UserEntity) {
        val ar_userAddr = userEntity.address!!.split("@")


        //1)생년월일
        binding.tvUserBirthDateMypage.text = userEntity.birth_date
        //2)닉네임
        binding.tvNicknameMypage.text = userEntity.nickname
        //3)거주지역
        binding.tvAddrMypage.text = ar_userAddr[0]

        //프로필 이미지가 있을 때만 이미지 로딩
        if (userEntity.profile_ImgUrl?.isNotEmpty() == true) {
            //4)프로필 사진
            activity?.run {
                Glide.with(this.applicationContext)
                    .load(Constants.Base_img_url + userEntity.profile_ImgUrl)
                    .override(500, 500)
                    .into(binding.ivProfileImgMypage)
            }
        }


    }


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


/*   //show, Hidden 이벤트
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        //화면이 보여질 때
        if (!hidden) {
        }
    }*/