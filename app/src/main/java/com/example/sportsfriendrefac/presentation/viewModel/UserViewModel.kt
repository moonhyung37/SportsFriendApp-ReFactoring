package com.example.sportsfriendrefac.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.base.BaseViewModel
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.domain.userUseCase.CertifiedEmailUseCase
import com.example.sportsfriendrefac.domain.userUseCase.LoginUseCase
import com.example.sportsfriendrefac.domain.userUseCase.RedundancyUseCase
import com.example.sportsfriendrefac.domain.userUseCase.RegisterUseCase
import com.example.sportsfriendrefac.util.Constants
import com.example.sportsfriendrefac.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
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


    //DataStore에 회원정보(id, 닉네임, (프로필사진)을 저장
    fun saveUserData(userData: String): String {

        val ar_userData = userData.split("@")
        //DatatStore에 저장
        runBlocking {
            App.instance.setStringData(Constants.USERIDKEY, ar_userData[0])
            App.instance.setStringData(Constants.USERNICKNAMEKEY, ar_userData[1])
            App.instance.setStringData(Constants.USERPROFILEIMGKEY, ar_userData[2])
        }
        return userData
    }

    //DataStore에 회원정보(id, 닉네임, (프로필사진)을 저장
    fun readUserData() {
        runBlocking {
            val id = App.instance.getStringData(Constants.USERIDKEY)
            val nick = App.instance.getStringData(Constants.USERNICKNAMEKEY)
            val profileImg = App.instance.getStringData(Constants.USERPROFILEIMGKEY)
            Timber.d("id: $id nick: $nick profileImg: $profileImg")
        }
    }

    //회원가입 유스케이스 실행 함수
    fun requestRegisterUser(userEntity: UserEntity) {
        //최종적인 API 통신 응답값을 LiveData에 입력
        //-Invoke fun 사용
        registerUseCase(userEntity, viewModelScope) {
            _LiveUser.postValue(Event(it))
        }
    }

    //이메일 인증 유스케이스 실행 함수
    fun certifiedEmail(email: String) {
        //최종적인 API 통신 응답값을 LiveData에 입력
        certifiedEmail(email, viewModelScope) {
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
    fun loginCheck(email: String, pw: String) {
        //최종적인 API 통신 응답값을 LiveData에 입력
        loginUseCase(email, pw, viewModelScope) {
            _LiveUser.postValue(Event(it))
        }


    }

    //자동 로그인
    fun checkAutoLogin(): Boolean {
        var flag = false
        runBlocking {
            //null인 경우 false
            flag = !App.instance.getStringData(Constants.USERIDKEY).isNullOrEmpty()
        }
        return flag

    }


}