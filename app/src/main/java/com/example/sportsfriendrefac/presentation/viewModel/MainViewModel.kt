package com.example.sportsfriendrefac.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseViewModel
import com.example.sportsfriendrefac.domain.bulletinUseCase.BulletinSelectUseCase
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.domain.userUseCase.SelectUserUseCase
import com.example.sportsfriendrefac.util.Event
import com.example.sportsfriendrefac.util.PageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bulletinSelectUseCase: BulletinSelectUseCase, //모집글 조회
    private val selectUserUseCase: SelectUserUseCase, //모집글 조회
) : BaseViewModel() {


    //처음 보여줄 프래그먼트 화면 설정
    private val _Live_pageType = MutableLiveData(PageType.PAGE1)
    val live_pageType: LiveData<PageType> = _Live_pageType

    //모집글 관련 LiveData
    private val _Live_bulletin = MutableLiveData<Event<List<BulletinEntity>>>()
    val live_bulletin: LiveData<Event<List<BulletinEntity>>> = _Live_bulletin
    // TODO: LiveData Sealed class 공부

    private val _sharedUser = MutableSharedFlow<EventUser>()
    val sharedUser = _sharedUser.asSharedFlow()

    /*모집 글 정보 조회 관련 코드*/


    /* 바텀네비게이션 프래그먼트 관련 코드 */
    //처음 화면에 보여줄  프레그먼트 세팅
    fun setCurrentPage(menuItemId: Int): Boolean {
        //선택한 페이지의 타입을 반환
        val pageType = getPageType(menuItemId)
        //페이지 타입을 이용해 화면을 전환
        changePageLiveData(pageType)
        return true
    }

    //선택한 프래그먼트 Id에 맞는 Fragment Type 반환
    private fun getPageType(menuItemId: Int): PageType {
        return when (menuItemId) {
            R.id.bulletinNavMenu -> PageType.PAGE1
            R.id.friendListNavMenu -> PageType.PAGE2
            R.id.chatRoomNavMenu -> PageType.PAGE3
            R.id.mypageNavMenu -> PageType.PAGE4
            else -> throw IllegalArgumentException("not found menu item id")
        }
    }

    //프래그먼트 전환
    private fun changePageLiveData(pageType: PageType) {
        //페이지 타입이 같은 경우 return
        if (live_pageType.value == pageType) return
        //다른 경우 PageType을 LiveData에 입력
        _Live_pageType.value = pageType
    }


    //모집 글 조회 함수
    fun selectAllBulletin() {
        //최종적인 API 통신 응답값을 LiveData에 입력
        bulletinSelectUseCase(viewModelScope) {
            _Live_bulletin.postValue(Event(it))
        }
    }

    //회원정보 조회 함수
    fun selectUserData(userId: String) {
        //최종적인 API 통신 응답값을 LiveData에 입력
        selectUserUseCase(userId, viewModelScope) {
            //eventUser객체에 서버에서 받은 데이터를 전달
            event(EventUser.UserData(it))
        }
    }

    //이벤트를 전달해주는 클래스
    sealed class EventUser {
        data class UserData(val userEntity: UserEntity) : EventUser()
    }


    //서버에서 보낸 이벤트를 SharedFlow에 전달해주는 메서드
    private fun event(eventUser: EventUser) {
        viewModelScope.launch {
            _sharedUser.emit(eventUser)
        }
    }

}