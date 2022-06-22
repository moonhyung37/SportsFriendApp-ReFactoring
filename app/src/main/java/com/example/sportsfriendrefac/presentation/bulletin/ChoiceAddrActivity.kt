package com.example.sportsfriendrefac.presentation.bulletin

import android.content.Intent
import android.view.View


import androidx.databinding.DataBindingUtil


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.databinding.ActivityChoiceAddrBinding
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.login.DaumAddrFragDialog
import com.example.sportsfriendrefac.presentation.login.LoginActivity

class ChoiceAddrActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityChoiceAddrBinding
    private var fragmentDialog: DaumAddrFragDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choice_addr)
        //다이얼로그 객체 생성
        fragmentDialog = DaumAddrFragDialog()
        //버튼클릭 리스너 등록
        binding.tvLiveAddrChoice.setOnClickListener(this)
        binding.tvInterestAddrChoice.setOnClickListener(this)
        binding.btnCompleteAddr.setOnClickListener(this)


        binding.tvLiveAddrChoice.text = intent.getStringExtra("liveAddr")
        if (intent.hasExtra("interestAddr")) {
            binding.tvInterestAddrChoice.text = intent.getStringExtra("interestAddr")
        }


        //다이얼로그에서 주소 클릭 시 실행
        //-다이얼로그안에 다음 주소선택 웹뷰가 있음
        fragmentDialog?.setAddressClickListener(object : DaumAddrFragDialog.OnclickListener {
            override fun onButtonClick(address: String?, flagAddr: Int) {
                if (address != null) {
                    //1번 거주지역
                    if (flagAddr == 1) {
                        binding.tvLiveAddrChoice.text = address
                    }
                    //2번 관심지역
                    if (flagAddr == 2) {
                        binding.tvInterestAddrChoice.text = address
                    }
                }
            }
        })
    }

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 거주지역
            binding.tvLiveAddrChoice.id -> {
                fragmentDialog?.showDialog(supportFragmentManager, "LiveAddr", 1)

            }

            //2) 관심지역
            binding.tvInterestAddrChoice.id -> {
                //주소검색 다이얼로그 뛰우기
                fragmentDialog?.showDialog(supportFragmentManager, "InterestAddr", 2)
            }
            //3) 선택완료
            binding.btnCompleteAddr.id -> {

                val intent =
                    Intent(applicationContext, MainActivity::class.java).apply {
                        putExtra("1", binding.tvLiveAddrChoice.text.toString()) //거주지역
                        putExtra("2", binding.tvInterestAddrChoice.text.toString()) //관심지역
                    }

                setResult(RESULT_OK, intent)
                if (!isFinishing) finish()

            }
            else -> {
            }
        }
    }
}