package com.example.sportsfriendrefac.presentation.bulletin

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View


import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfriendref.BulletinRvAdapter
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.databinding.FragmentBulletinBinding
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.extension.repeatOnStarted
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.viewModel.MainViewModel


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

    //거주지역, 관심지역 수정에 필요한 객체
    private var userEntity: UserEntity? = null

    //주소 정보를 표시하는 스피너
    val spinnerAdapter by lazy {
        activity?.applicationContext?.let {
            ArrayAdapter(it,
                R.layout.spinner_item_addr,
                viewModel.list_addrSpinner)
        }
    }

    //Class MainActivity
    lateinit var activityResultBulletinInfor: ActivityResultLauncher<Intent>

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
        userIdx = (activity as MainActivity).userIdx

        setRecyclerView()
        setAddBulletinFragmentDialog()


        //모집글 관련 이벤트를 감지(C,R,U,D)
        repeatOnStarted {
            viewModel.sharedBulletin.collect { event ->
                receiveEvent(event)
            }
        }

        //oncreate에서 호출해야함.
//엑티비티에서 받아온 데이터 받기
        activityResultBulletinInfor =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //엑티비티에서 데이터를 갖고왔을 때만 실행
                if (it.resultCode == RESULT_OK) {
                    //SubActivity에서 갖고온 Intent(It)
                    val myData: Intent? = it.data
                    val address = it.data?.getStringExtra("키") ?: ""
                }
            }

        //회원정보 조회
        viewModel.selectUserDataBulletin(userIdx ?: "")
    }


    override fun onResume() {
        super.onResume()
        //모집글의 수정, 삭제가 일어났을 때 전체 모집글을 갱신 시켜준다.
        if (App.instance.flagBulletinSelect == 1) {
            //전체 모집글 정보 조회
            viewModel.selectAllBulletin(userIdx ?: "", 1, "")
            App.instance.flagBulletinSelect = 0
        }

    }


    //관심지역 + 거주지역 선택 스피너 세팅
    private fun setSpinner(userEntity: UserEntity) {
        this.userEntity = userEntity
        viewModel.list_addrSpinner.clear()
        val ar_userAddr = userEntity.address!!.split("$")
        val spinner_keyword_allAddr = "전체보기"
        viewModel.liveAddr = ar_userAddr[0] // 거주지역
        viewModel.interestAddr = "" //관심지역

        //스피너에 사용할 텍스트 넣어주기
        viewModel.list_addrSpinner.add(spinner_keyword_allAddr)
        viewModel.list_addrSpinner.add(viewModel.liveAddr)
        //관심지역을 선택한 경우에만 사용
        if (ar_userAddr.size == 2) {
            viewModel.interestAddr = ar_userAddr[1]
            viewModel.list_addrSpinner.add(viewModel.interestAddr)
        }

        //지역수정 후 모집 글 새로 조회하기


        //스피너 세팅
        binding.spinnerMain.adapter = spinnerAdapter
        binding.spinnerMain.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //아이템을 선택했을 때 실행
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                //모집글 초기화
                bulletinRvAdapter?.clear()
                when {
                    //1)스피너에서 "전체 보기"를 선택한 경우
                    /* 모집 글이 처음 조회되는 시점 */
                    viewModel.list_addrSpinner[position] == spinner_keyword_allAddr -> {
                        //전체 모집글 정보 조회
                        viewModel.selectAllBulletin(userIdx ?: "", 1, "")
                    }

                    //2)스피너에서 "거주지역"을 선택한 경우
                    viewModel.list_addrSpinner[position] == viewModel.liveAddr -> {
                        //전체 모집글 정보 조회
                        viewModel.selectAllBulletin(userIdx ?: "", 2, viewModel.liveAddr)
                    }

                    //3)스피너에서 "관심지역"을 선택한 경우
                    viewModel.list_addrSpinner[position] == viewModel.interestAddr -> {
                        //전체 모집글 정보 조회
                        viewModel.selectAllBulletin(userIdx ?: "", 3, viewModel.interestAddr)
                    }


                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


    //모집글 추가 프레그먼트 다이얼로그 세팅
    //- 입력한 모집글 정보를 갖고온다.
    private fun setAddBulletinFragmentDialog() {
        fragmentDialog = AddBulletinFragDialog()
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
            override fun onItemClick(v: View, data: BulletinEntity, pos: Int) {
                val intent = Intent(activity?.applicationContext, BulletinInforActivity::class.java)
                intent.putExtra("1", data.idx)
                activityResultBulletinInfor.launch(intent)
            }
        })


    }

    //ViewModel에서 보낸 이벤트 처리
    private fun receiveEvent(eventBulletinSealed: MainViewModel.EventBulletinSealed) =
        when (eventBulletinSealed) {
            //모집글 정보 조회
            is MainViewModel.EventBulletinSealed.BulletinSelect -> selectBulletinDataUi(
                eventBulletinSealed.List_bulletinEntity)

            //모집 글 추가
            is MainViewModel.EventBulletinSealed.BulletinAdd -> addBulletinDataUi(
                eventBulletinSealed.bulletinEntity)
            //회원정보 조회
            //- 회원정보가 응답값으로 오면 관심지역, 거주지역을 스피너에 세팅하기 위해서 사용
            is MainViewModel.EventBulletinSealed.UserData -> setSpinner(eventBulletinSealed.userEntity)
        }

    /* 서버에서 받은 모집 글 정보 처리 메서드*/
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












