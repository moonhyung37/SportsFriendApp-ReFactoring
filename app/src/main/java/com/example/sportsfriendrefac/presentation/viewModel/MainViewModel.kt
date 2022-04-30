package com.example.sportsfriendrefac.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseViewModel
import com.example.sportsfriendrefac.util.PageType
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {


    //처음 보여줄 프래그먼트 화면 설정
    private val _Live_pageType = MutableLiveData(PageType.PAGE1)

    val live_pageType: LiveData<PageType> = _Live_pageType


    //처음 화면에 보여줄  프레그먼트 세팅
    fun setCurrentPage(menuItemId: Int): Boolean {
        val pageType = getPageType(menuItemId)
        changePageLiveData(pageType)
        return true
    }

    //선택한 프래그먼트 Id에 맞는 Fragment Type 반환
    private fun getPageType(menuItemId: Int): PageType {
        return when (menuItemId) {
            R.id.bulletinMenu -> PageType.PAGE1
            R.id.friendListMenu -> PageType.PAGE2
            R.id.chatRoomMenu -> PageType.PAGE3
            else -> throw IllegalArgumentException("not found menu item id")
        }
    }

    //프래그먼트 전환
    private fun changePageLiveData(pageType: PageType) {
        //페이지 타입이 같은 경우 return
        if (live_pageType.value == pageType) return
        //다른 경우 LiveData에 입력
        _Live_pageType.value = pageType
    }

}