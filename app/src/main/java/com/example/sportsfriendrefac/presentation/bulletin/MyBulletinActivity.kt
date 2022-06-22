package com.example.sportsfriendrefac.presentation.bulletin

import android.widget.PopupMenu


import android.view.View


import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.sportsfriendref.BulletinRvAdapter
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseActivity
import com.example.sportsfriendrefac.databinding.ActivityMyBulletinBinding
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.extension.repeatOnStarted
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.viewModel.MyBulletinViewModel
import com.example.sportsfriendrefac.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@AndroidEntryPoint
class MyBulletinActivity : BaseActivity<ActivityMyBulletinBinding, MyBulletinViewModel>
    (R.layout.activity_my_bulletin), View.OnClickListener {
    private lateinit var bulletinRvAdapter: BulletinRvAdapter
    override val viewModel: MyBulletinViewModel by viewModels()
    override val TAG: String
        get() = "MyBulletinActivity"

    var userIdx: String? = null

    //모집 글 수정시 필요한 변수
    //-모집 글 수정 팝업 뷰 클릭 시 변수 할당
    //-서버에서 응답받은 데이터로 모집글 리사이클러뷰를 수정 시 사용
    var itemPosition: Int = 0 //수정하려는 모집글의 포지션 번호

    //모집 글 수정에 사용 (서버에 요청하는 데이터)
    var bltn_idx: String? = null //모집글의 idx
    var comment_cnt: String? = null //모집글 댓글 개수

    lateinit var activityResultEditBulletin: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //버튼클릭 리스너 등록
        binding.ivBackiconMyBulletin.setOnClickListener(this)
        //리사이클러뷰 세팅
        setRecyclerView()
        runBlocking {
            //회원정보 idx
            userIdx = App.instance.getStringData(Constants.USERIDKEY)
        }


        //내가 작성한 모집글 관련 이벤트를 감지(C,R,U,D)
        repeatOnStarted {
            viewModel.sharedBulletin.collect { event ->
                receiveMyBulletinEvent(event)
            }
        }


        activityResultEditBulletin()
        //내가 작성한 모집 글 정보 유스케이스 실행
        viewModel.selectMyBulletin(userIdx ?: "")
    }


    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 뒤로가기
            binding.ivBackiconMyBulletin.id -> {
                if (!isFinishing) finish()
            }
            else -> {
            }
        }
    }

    //viewModel에서 전달한 Event객체에 따라서 값을 처리하는 메서드(모집 글 조회, 삭제, 수정)
    private fun receiveMyBulletinEvent(eventBulletinSealed: MyBulletinViewModel.EventMyBulletinSealed) =
        when (eventBulletinSealed) {
            //1)내가 작성한 모집글 정보 조회
            is MyBulletinViewModel.EventMyBulletinSealed.MyBulletinSelect -> bulletinRvAdapter.submitList(
                eventBulletinSealed.List_bulletinEntity)
            //2)내가 작성한 모집 글 삭제
            is MyBulletinViewModel.EventMyBulletinSealed.MyBulletinDelete -> Timber.d(
                "bltn_idx: " + eventBulletinSealed.bltn_idx)
            //3)내가 작성한 모집 글 수정
            //-리사이클러뷰 수정
            is MyBulletinViewModel.EventMyBulletinSealed.MyBulletinUpdate -> updateRvItemView(
                eventBulletinSealed.bulletinEntity)
        }

    private fun updateRvItemView(bulletinEntity: BulletinEntity) {
        //모집글의 댓글 개수 입력
        // -bulletin 테이블엔 댓글 개수가 없어서 사용함.
        bulletinEntity.comment_cnt = comment_cnt
        bulletinRvAdapter.editItem(itemPosition, bulletinEntity)
    }

    //리사이클러뷰 세팅
    private fun setRecyclerView() {
        binding.rvMyBulletin.apply {
            bulletinRvAdapter = BulletinRvAdapter()
            applicationContext?.run { bulletinRvAdapter.setContext(this) }
            adapter = bulletinRvAdapter
        }
        /*리사이클러뷰 클릭 리스너 */
        bulletinRvAdapter.setOnItemClickListener(object : BulletinRvAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: BulletinEntity, pos: Int) {
                //팝업뷰 클릭
                val pop = PopupMenu(applicationContext, v)
                menuInflater.inflate(R.menu.popup_menu, pop.menu)
                //팝업 뷰 리스너
                pop.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        //수정
                        R.id.edit_popup -> {
                            itemPosition = pos
                            bltn_idx = data.idx
                            comment_cnt = data.comment_cnt
                            val intent =
                                Intent(applicationContext, EditBulletinActivity::class.java)
                            //이전에 입력한 모집글의 정보 보내기
                            intent.putExtra("1", data.bltn_title)
                            intent.putExtra("2", data.bltn_content)
                            intent.putExtra("3", data.bltn_addr)
                            intent.putExtra("4", data.bltn_exer)
                            intent.putExtra("5", data.bltn_img_url)
                            activityResultEditBulletin.launch(intent)
                        }
                        //삭제
                        R.id.delete_popup -> {
                            //리사이클러뷰에 모집 글 삭제
                            bulletinRvAdapter.deleteItem(pos)
                            viewModel.deleteMyBulletin(data.idx)
                        }
                    }
                    false
                }
                pop.show()
            }
        })

    }

    //모집글 수정 정보 받아오기
    private fun activityResultEditBulletin() {
        //수정한 엑티비티 정보 받아오기
        activityResultEditBulletin = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            //엑티비티에서 데이터를 갖고왔을 때만 실행
            if (it.resultCode == RESULT_OK) {

                val title = it.data?.getStringExtra("1") ?: ""
                val content = it.data?.getStringExtra("2") ?: ""
                val address = it.data?.getStringExtra("3") ?: ""
                val exercise = it.data?.getStringExtra("4") ?: ""
                val imgUrl = it.data?.getStringExtra("5") ?: ""

                //모집 글 수정 유스케이스 실행
                viewModel.updateMyBulletin(BulletinEntity(
                    bltn_idx ?: "", //서버에 요청 데이터1
                    "",
                    "",
                    title, //서버에 요청 데이터2
                    content, //서버에 요청 데이터3
                    imgUrl, //서버에 요청 데이터4
                    exercise, //서버에 요청 데이터5
                    address, //서버에 요청 데이터6
                    2,
                    ""
                ), applicationContext)
            }
        }
    }

}