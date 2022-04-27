package com.example.sportsfriendrefac.presentation.login


import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseActivity
import com.example.sportsfriendrefac.databinding.ActivityLoginBinding
import com.example.sportsfriendrefac.presentation.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
//BaseActivity 상속
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>
//레이아웃 경로 입력
    (R.layout.activity_login) {
    override val viewModel: LoginViewModel by viewModels()
    var toolbarTitle: TextView? = null
    override val TAG: String
        get() = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarTitle = binding.tvToolbarTitle
        setSupportActionBar(binding.myToolbar)    //툴바 사용 설정
        //액션바 타이틀 없애기
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }


}
