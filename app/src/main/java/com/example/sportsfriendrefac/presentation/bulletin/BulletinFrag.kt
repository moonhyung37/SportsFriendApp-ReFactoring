package com.example.sportsfriendrefac.presentation.bulletin

import android.view.View


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.sportsfriendref.BulletinRvAdapter
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.databinding.FragmentBulletinBinding
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.extension.repeatOnStarted
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.viewModel.MainViewModel
import timber.log.Timber


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BulletinFrag : BaseFragment<FragmentBulletinBinding>(R.layout.fragment_bulletin),
    View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: MainViewModel by activityViewModels<MainViewModel>()

    private var bulletinRvAdapter: BulletinRvAdapter? = null

    //모집글 추가/수정 프래그먼트 다이얼로그
    private var fragmentDialog: AddBulletinFragDialog? = null
    var userIdx: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }


    override fun init() {
        //버튼클릭 리스너 등록
        binding.ivSearchBulletin.setOnClickListener(this)
        binding.ivWriteBulletin.setOnClickListener(this)
        fragmentDialog = AddBulletinFragDialog()
        userIdx = (activity as MainActivity).userIdx

        setRecyclerView()
        setAddBulletinListener()

        //모집글 관련 이벤트를 감지(C,R,U,D)
        repeatOnStarted {
            viewModel.sharedBulletin.collect { event ->
                receiveBulletinEvent(event)
            }
        }

        viewModel.selectAllBulletin(userIdx ?: "")
    }

    override fun onResume() {
        super.onResume()
        //모집글의 수정, 삭제가 일어났을 때 전체 모집글을 갱신 시켜준다.
        if (App.instance.flagBulletinSelect == 1) {
            viewModel.selectAllBulletin(userIdx ?: "")
            App.instance.flagBulletinSelect = 0
        }

    }


    //모집 글 추가 버튼 클릭 리스너
    // -입력한 모집글 정보를 갖고온다.
    private fun setAddBulletinListener() {
        fragmentDialog?.setBulletinClickListener(object : AddBulletinFragDialog.OnclickListener {
            override fun onButtonClick(bulletinEntity: BulletinEntity) {
                //모집글 추가 유스케이스 실행
                activity?.applicationContext?.let { viewModel.addBulletin(bulletinEntity, it) }
            }
        })
    }

    //리사이클러뷰 세팅
    private fun setRecyclerView() {
        bulletinRvAdapter = BulletinRvAdapter()
        binding.rvBulletinRv.apply {
            //Context 받아오기
            activity?.applicationContext?.run { bulletinRvAdapter?.setContext(this) }
            adapter = bulletinRvAdapter
        }

        /*리사이클러뷰 클릭 리스너 */
        bulletinRvAdapter?.setOnItemClickListener(object : BulletinRvAdapter.OnItemClickListener {
            override fun onItemClick(v: View, bulletinEntity: BulletinEntity, pos: Int) {

            }
        })
    }

    //viewModel에서 전달한 Event객체에 따라서 값을 처리하는 메서드
    private fun receiveBulletinEvent(eventBulletinSealed: MainViewModel.EventBulletinSealed) =
        when (eventBulletinSealed) {
            //모집글 정보 조회
            is MainViewModel.EventBulletinSealed.BulletinSelect -> selectBulletinDataUi(
                eventBulletinSealed.List_bulletinEntity)
            //모집 글 추가
            is MainViewModel.EventBulletinSealed.BulletinAdd -> addBulletinDataUi(
                eventBulletinSealed.bulletinEntity)
        }

    //서버에서 받은 모집글 정보를 리사이클러뷰에 입력해주는 메서드(모집글 조회)
    private fun selectBulletinDataUi(List_bulletinEntity: List<BulletinEntity>) {
        bulletinRvAdapter?.submitList(List_bulletinEntity)
    }

    //서버에서 받은 추가한 모집글 정보를 리사이클러뷰에 입력해주는 메서드(모집글 추가)
    private fun addBulletinDataUi(bulletinEntity: BulletinEntity) {
        Toast.makeText(activity?.applicationContext, "모집글이 작성되었습니다.", Toast.LENGTH_SHORT).show()
        bulletinRvAdapter?.addItem(bulletinEntity)
    }


    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 모집글 생성 이미지 버튼
            binding.ivWriteBulletin.id -> {
                (activity as MainActivity?)?.supportFragmentManager?.let {
                    //1번 모집글 생성
                    fragmentDialog?.showAddBulletinDialog(it, "LiveAddr")
                }
            }


            //2) 모집글 검색 이미지 버튼
            binding.ivSearchBulletin.id,
            -> {

            }
            else -> {
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            //기본 생성자 생성
            BulletinFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


/* //show, Hidden 이벤트
   override fun onHiddenChanged(hidden: Boolean) {
       super.onHiddenChanged(hidden)
       if (!hidden) {
       }
   }*/