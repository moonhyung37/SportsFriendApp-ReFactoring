package com.example.sportsfriendrefac.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sportsfriendrefac.base.BaseViewModel
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.domain.useCase.RegisterUseCase
import com.example.sportsfriendrefac.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    //Hilt로 UseCase 주입
    private val registerUseCase: RegisterUseCase,
) : BaseViewModel() {

    //쓰기, 읽기, 수정 가능
    private val _LiveRegister = MutableLiveData<ApiResult<User>>()

    //읽기만 가능
    val liveRegister: LiveData<ApiResult<User>> = _LiveRegister

    fun requestRegisterUser(user: User) {

        //최종적인 API 통신 응답값을 받아온다.
        //람다함수 사용
        registerUseCase(user, viewModelScope) {
            _LiveRegister.value = it
        }
    }
}