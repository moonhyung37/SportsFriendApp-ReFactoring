package com.example.sportsfriendrefac.presentation.viewModel

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sportsfriendrefac.base.BaseViewModel
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.domain.loginUseCase.CertifiedEmailUseCase
import com.example.sportsfriendrefac.domain.loginUseCase.LoginUseCase
import com.example.sportsfriendrefac.domain.loginUseCase.RedundancyUseCase
import com.example.sportsfriendrefac.domain.loginUseCase.RegisterUseCase
import com.example.sportsfriendrefac.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    //Hilt로 UseCase 주입
    private val registerUseCase: RegisterUseCase, //회원가입
    private val certifiedEmail: CertifiedEmailUseCase, //이메일인증
    private val redundancyUseCase: RedundancyUseCase, //중복검사
    private val loginUseCase: LoginUseCase,
) : BaseViewModel() {


    //회원가입, 로그인, 이메일인증, 중복처리
    private val _LiveUser = MutableLiveData<Event<String>>()

    val liveUser: LiveData<Event<String>>
        get() = _LiveUser


    //회원가입 유스케이스 실행 함수
    fun requestRegisterUser(user: User) {
        //최종적인 API 통신 응답값을 LiveData에 입력
        //-Invoke fun 사용
        registerUseCase(user, viewModelScope) {
            _LiveUser.postValue(Event(it))
        }
    }


    //이메일 인증 유스케이스 실행 함수
    fun certifiedEmail(user: User) {
        //최종적인 API 통신 응답값을 LiveData에 입력
        certifiedEmail(user, viewModelScope) {
            _LiveUser.postValue(Event(it))
        }
    }

    //중복검사 유스케이스 실행 함수
    fun redundancyCheck(checkData: String) {
        //최종적인 API 통신 응답값을 LiveData에 입력
        redundancyUseCase(checkData, viewModelScope) {
            //비동기적인 값을 받음
            _LiveUser.postValue(Event(it))
        }
    }

    //로그인 유스케이스 실행 함수
    fun loginCheck(user: User) {
        //최종적인 API 통신 응답값을 LiveData에 입력
        loginUseCase(user, viewModelScope) {
            //비동기적인 값을 받음
            _LiveUser.postValue(Event(it))
        }
    }



}