package com.example.sportsfriendrefac.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseActivity
import com.example.sportsfriendrefac.databinding.ActivityMainBinding
import com.example.sportsfriendrefac.presentation.bulletin.BulletinFrag
import com.example.sportsfriendrefac.presentation.chating.ChatRoomFrag
import com.example.sportsfriendrefac.presentation.friend.FriendListFrag
import com.example.sportsfriendrefac.presentation.user.MypageFrag
import com.example.sportsfriendrefac.presentation.viewModel.MainViewModel
import com.example.sportsfriendrefac.util.Constants
import com.example.sportsfriendrefac.util.PageType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>
    (R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()
    override val TAG: String
        get() = "MainActivity"

    //나의 idx
    var userIdx: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel

        //바텀네비게이션 이동 이벤트를 감지
        viewModel.live_pageType.observe(this) {
            //선택한 프래그먼트의 PageType을 반환
            changeFragment(it)
        }

        runBlocking {
            //회원정보 idx
            userIdx = App.instance.getStringData(Constants.USERIDKEY)
        }

    }

    /* 프래그먼트 바텀내비게이션 함수
    *  -프래그먼트 재생성 관련 메모리 낭비를 줄이기위해 사용
    * */
    //프래그먼트 전환 함수
    private fun changeFragment(pageType: PageType) {
        //프래그먼트 바텀네비게이션 클릭 변경
        val transaction = supportFragmentManager.beginTransaction()
        //프래그먼트에 매니저에 저장된  프래그먼트를 갖고온다.
        var targetFragment = supportFragmentManager.findFragmentByTag(pageType.tag)

        //저장된 프래그먼트가 없는 경우
        if (targetFragment == null) {
            //프래그먼트 생성
            targetFragment = getFragment(pageType)
            //작업을 처리하는 트랜잭션에 추가
            transaction.add(R.id.fl_container_Main, targetFragment, pageType.tag)
        }

        transaction.show(targetFragment)

        //나머지 프래그먼트 숨기기
        PageType.values()
            .filterNot { it == pageType }
            .forEach { type ->
                supportFragmentManager.findFragmentByTag(type.tag)?.let {
                    transaction.hide(it)
                }
            }

        //illeagalStateException 방지
        transaction.commitAllowingStateLoss()
    }

    //타입에 맞는 프래그먼트 반환
    private fun getFragment(pageType: PageType): Fragment {
        lateinit var fragment: Fragment


        //페이지 타입에 맞는 프래그먼트 객체 반환
        when (pageType.tag) {
            //1)모집 글 목록 프래그먼트
            resources.getString(R.string.bulletinFragmentTag1) -> {
                fragment = BulletinFrag.newInstance("", "")
            }

            //2)친구 목록 프래그먼트
            resources.getString(R.string.friendFragmentTag2) -> {
                fragment = FriendListFrag.newInstance("", "")
            }

            //3)채팅방 목록 프래그먼트
            resources.getString(R.string.chatRoomFragmentTag3) -> {
                fragment = ChatRoomFrag.newInstance("", "")
            }

            //4)마이페이지
            resources.getString(R.string.myPageFragmentTag) -> {
                fragment = MypageFrag.newInstance("", "")
            }
            else -> {

            }
        }
        return fragment
    }
}


/* 바텀네비게이션 아이콘 클릭 처리 관련코드
  if (selectIntentData == selectOption) {
            pageType.tag = resources.getString(R.string.myPageFragmentTag)
            val weakReference = WeakReference(binding.bnvMain)
            val view = weakReference.get()
            view?.menu?.findItem(R.id.mypageNavMenu)?.isChecked = true
            selectOption = null
        }*/
