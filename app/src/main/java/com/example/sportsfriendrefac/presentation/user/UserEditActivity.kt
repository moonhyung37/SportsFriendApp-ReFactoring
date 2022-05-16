package com.example.sportsfriendrefac.presentation.user

import android.content.Intent
import androidx.databinding.DataBindingUtil


import android.view.View


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.databinding.ActivityUserEditBinding
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.login.DaumAddrFragDialog

class UserEditActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserEditBinding
    private var fragmentDialog: DaumAddrFragDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_edit)
        //다이얼로그 객체 생성
        fragmentDialog = DaumAddrFragDialog()

        //버튼클릭 리스너 등록
        binding.ivBackiconEditMyapge.setOnClickListener(this)
        binding.tvLiveAddrEditMypage.setOnClickListener(this)
        binding.tvInterestAddrEditMypage.setOnClickListener(this)

        //회원정보 갖고오기
        if (intent.hasExtra("1")) {
            //닉네임
            binding.tvNicknameEditMypage.setText(intent.getStringExtra("1").toString())
            //생년월일
            binding.tvBirthDateEditMypage.setText(intent.getStringExtra("2").toString())
            //거주지역
            binding.tvLiveAddrEditMypage.text = intent.getStringExtra("3")
            //관심지역
            binding.tvInterestAddrEditMypage.text = intent.getStringExtra("4") ?: ""
            //상태메세지
            binding.tvContentEditMypage.setText(intent.getStringExtra("5").toString())
        }


        //다이얼로그에서 주소 클릭 시 실행
        //-다이얼로그안에 다음 주소선택 웹뷰가 있음
        fragmentDialog?.setAddressClickListener(object : DaumAddrFragDialog.OnclickListener {
            override fun onButtonClick(address: String?, flagAddr: Int) {
                if (address != null) {
                    //1번 거주지역
                    if (flagAddr == 1) {
                        binding.tvLiveAddrEditMypage.text = address
                    }
                    //2번 관심지역
                    if (flagAddr == 2) {
                        binding.tvInterestAddrEditMypage.text = address
                    }

                }
            }
        })
    }

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 뒤로가기
            binding.ivBackiconEditMyapge.id -> {
                //입력한 회원정보 마이페이지 엑티비티에 보내기
                val intent = Intent(applicationContext, MainActivity::class.java).apply {
                    putExtra("1", binding.tvNicknameEditMypage.text.toString())//닉네임
                    putExtra("2", binding.tvBirthDateEditMypage.text.toString())//생년월일
                    putExtra("3", binding.tvLiveAddrEditMypage.text.toString())//거주지역
                    putExtra("4", binding.tvInterestAddrEditMypage.text.toString())//관심지역
                    putExtra("5", binding.tvContentEditMypage.text.toString())//상태메세지
                }
                setResult(RESULT_OK, intent)
                if (!isFinishing) finish()
            }

            //2)거주지역 수정
            binding.tvLiveAddrEditMypage.id -> {
                //프래그먼트로된 다이얼로그 주소검색 웹뷰 뛰우기
                supportFragmentManager.run {
                    fragmentDialog?.showDialog(this, "LiveAddr", 1)
                }
            }

            //3)관심지역 수정
            binding.tvInterestAddrEditMypage.id -> {
                binding.tvInterestAddrEditMypage.text = ""
                //프래그먼트로된 다이얼로그 주소검색 웹뷰 뛰우기
                supportFragmentManager.run {
                    fragmentDialog?.showDialog(this, "InterestAddr", 2)
                }
            }


            else -> {
            }
        }
    }
}