package com.example.sportsfriendrefac.presentation

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseActivity
import com.example.sportsfriendrefac.databinding.ActivityMainBinding
import com.example.sportsfriendrefac.presentation.bulletin.BulletinFrag
import com.example.sportsfriendrefac.presentation.chating.ChatRoomFrag
import com.example.sportsfriendrefac.presentation.friend.FriendListFrag
import com.example.sportsfriendrefac.presentation.viewModel.MainViewModel
import com.example.sportsfriendrefac.util.PageType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>
    (R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()
    override val TAG: String
        get() = "MainActivity"
    var toolbarTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setSupportActionBar(binding.myToolbarMain)    //툴바 사용 설정
        toolbarTitle = binding.tvToolbarTitleMain
        //액션바 타이틀 없애기
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.vm = viewModel


        viewModel.live_pageType.observe(this) {
            //선택한 프래그먼트의 PageType을 반환
            changeFragment(it)
        }

    }

    private fun changeFragment(pageType: PageType) {
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

    //페이지 타입에 맞는 프래그먼트 객체 반환
    private fun getFragment(pageType: PageType): Fragment {
        lateinit var fragment: Fragment
        when (pageType.tag) {
            //1)모집 글 목록 프래그먼트
            resources.getString(R.string.bulletinFragmentTag1) -> {
                fragment = BulletinFrag.newInstance(pageType.title, "")
            }

            //2)친구 목록 프래그먼트
            resources.getString(R.string.friendFragmentTag2) -> {
                fragment = FriendListFrag.newInstance(pageType.title, "")
            }

            //3)채팅방 목록 프래그먼트
            resources.getString(R.string.chatRoomFragmentTag3) -> {
                fragment = ChatRoomFrag.newInstance(pageType.title, "")
            }
            else -> {}
        }

        return fragment


    }

}