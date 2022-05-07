package com.example.sportsfriendrefac.presentation.user

import android.content.Intent
import androidx.databinding.DataBindingUtil


import android.view.View


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.databinding.ActivityUserEditBinding
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.util.Constants

class UserEditActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_edit)


        //버튼클릭 리스너 등록
        binding.ivBackiconEditMyapge.setOnClickListener(this)


    }

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 뒤로가기
            binding.ivBackiconEditMyapge.id -> {
                finish()
                /*val intent = Intent(applicationContext, MainActivity::class.java)
                intent.putExtra(Constants.SELECTKEY, "MYPAGE")
                startActivity(intent)
                finish()*/
            }
            else -> {
            }
        }
    }
}