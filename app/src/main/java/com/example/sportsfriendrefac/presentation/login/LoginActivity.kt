package com.example.sportsfriendrefac.presentation.login


import androidx.databinding.DataBindingUtil


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseActivity
import com.example.sportsfriendrefac.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
//BaseActivity 상속
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>
//레이아웃 경로 입력
    (R.layout.activity_login) {
    override val viewModel: LoginViewModel by viewModels()

    override val TAG: String
        get() = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}
