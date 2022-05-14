package com.example.sportsfriendrefac.presentation.user

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.view.View


import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.databinding.FragmentMypageBinding
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.extension.repeatOnStarted
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.bulletin.MyBulletinActivity
import com.example.sportsfriendrefac.presentation.login.LoginActivity
import com.example.sportsfriendrefac.presentation.viewModel.MainViewModel
import com.example.sportsfriendrefac.util.Constants
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.runBlocking
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MypageFrag : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage),
    View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: MainViewModel by activityViewModels<MainViewModel>()

    //엑티비티에서 결과 값 받아오기
    lateinit var activityResultUserData: ActivityResultLauncher<Intent>
    lateinit var activityResultImage: ActivityResultLauncher<Intent>
    lateinit var activityResultMyBulletin: ActivityResultLauncher<Intent>

    var userIdx: String? = null

    var userEntity: UserEntity? = null

    //갤러리 관련 권한 리스너
    var permissionListener: PermissionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun init() {
        //버튼 클릭리스너 세팅
        binding.tvEditMypage.setOnClickListener(this)
        binding.liEditImgMypage.setOnClickListener(this)
        binding.liMyBulletinMypage.setOnClickListener(this)
        binding.btnDeleteMypage.setOnClickListener(this)
        binding.btnLogoutMypage.setOnClickListener(this)
        //회원탈퇴 버튼 숨기기
        binding.btnDeleteMypage.visibility = View.INVISIBLE


        //DataStore에 저장된 회원정보 idx
        userIdx = (activity as MainActivity?)?.userIdx

        //회원정보 서버에 요청
        userIdx?.run { viewModel.selectUserData(this) }

        //ViewModel에서 보내는 이벤트를 감지.
        //repeateOnstarted블럭 사용시 onStop이 되면 자동으로 이벤트 비활성화
        repeatOnStarted {
            viewModel.sharedUser.collect { event ->
                handleEvent(event)
            }
        }

        launchActivityResultUserData()
        launchActivityResultImage()
        launchActivityResultMyBulletin()

        //이미지 읽기, 쓰기 권한 세팅
        permissionListener = setImagePermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userIdx = null
        userEntity = null
        permissionListener = null

    }

    //viewModel에서 전달한 Event객체에 따라서 값을 처리하는 메서드
    private fun handleEvent(eventUserSealed: MainViewModel.EventUserSealed) =
        when (eventUserSealed) {
            //회원정보  조회
            is MainViewModel.EventUserSealed.UserData -> setUserDataUi(eventUserSealed.userEntity)
            //회원정보 수정
            is MainViewModel.EventUserSealed.UserUpdate -> Timber.d(eventUserSealed.message)
        }

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1)회원정보 수정
            binding.tvEditMypage.id -> {
                //주소 배열(거주지역@관심지역)
                val ar_userAddr = userEntity?.address!!.split("@")
                val intent = Intent(activity?.applicationContext, UserEditActivity::class.java)
                intent.putExtra("1", userEntity?.nickname) //닉네임
                intent.putExtra("2", userEntity?.birth_date) //생년월일
                intent.putExtra("3", ar_userAddr[0]) //거주지역
                intent.putExtra("4", ar_userAddr[1]) //관심지역
                intent.putExtra("5", userEntity?.content) //상태메세지
                activityResultUserData.launch(intent)
            }

            //2)회원 프로필 사진 수정
            binding.liEditImgMypage.id -> {
                permissionListener?.let { checkPermission(it) }
            }

            //3)내가 작성한 모집글 확인
            binding.liMyBulletinMypage.id -> {
                val intent = Intent(activity?.applicationContext, MyBulletinActivity::class.java)
                activityResultMyBulletin.launch(intent)
            }
            //4)회원탈퇴
            binding.btnDeleteMypage.id -> {

            }
            //5)로그아웃
            binding.btnLogoutMypage.id -> {
                runBlocking {
                    //DataStore에 저장된 회원정보 초기화(자동로그인 제거)
                    App.instance.clearDataStore()
                    //로그인 엑티비티로 이동
                    val intent = Intent(activity?.applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }

            else -> {
            }
        }
    }


    //내가 작성한 모집글 엑티비티에서 결과값 받아오기
    private fun launchActivityResultMyBulletin() {
        activityResultMyBulletin =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //엑티비티에서 데이터를 갖고왔을 때만 실행
                if (it.resultCode == RESULT_OK) {
                    //SubActivity에서 갖고온 Intent(It)
                    val myData: Intent? = it.data
                    val address = it.data?.getStringExtra("키") ?: ""
                }
            }
    }

    //회원정보 수정 엑티비티에서 결과값 받아오기
    private fun launchActivityResultUserData() {
        //UserEditActivity(회원정보 수정)엑티비티에서 회원정보 받아오기
        activityResultUserData =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //엑티비티에서 데이터를 갖고왔을 때만 실행
                if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    //수정할 회원정보
                    val nickname = it.data?.getStringExtra("1") ?: ""
                    val birthDate = it.data?.getStringExtra("2") ?: ""
                    val liveAddr = it.data?.getStringExtra("3") ?: ""
                    val interestAddr = it.data?.getStringExtra("4") ?: ""
                    val content = it.data?.getStringExtra("5") ?: ""

                    //처음 조회한 회원정보 수정
                    //이유: 수정하고 나서 바로 수정을 한번 더 할 때 처음 조회한 회원정보를 사용함으로 수정이 필요
                    userEntity?.nickname = nickname
                    userEntity?.birth_date = birthDate
                    userEntity?.address = "$liveAddr@$interestAddr"
                    userEntity?.content = content

                    //회원정보 Ui 수정
                    binding.tvNicknameMypage.text = nickname
                    binding.tvAddrMypage.text = liveAddr
                    binding.tvUserBirthDateMypage.text = birthDate

                    //회원정보 수정 유스케이스 실행
                    viewModel.updateUserData(UserEntity(
                        userIdx ?: "",
                        "",
                        "",
                        nickname,
                        "",
                        "",
                        "$liveAddr@$interestAddr",
                        birthDate,
                        content,
                        ""
                    ))
                }
            }
    }

    private fun launchActivityResultImage() {
        activityResultImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //엑티비티에서 데이터를 갖고왔을 때만 실행
                if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    it.data?.data?.let { uri ->
                        //이미지 uri
                        val imageUri: Uri? = it.data?.data
                        //이미지 uri가 null 이 아닐때만 리스트에 추가.
                        if (imageUri != null) {
                            Glide.with(this).load(imageUri).override(500, 500)
                                .into(binding.ivProfileImgMypage)

                            //회원 프로필 이미지 수정 유스케이스 실행
                            activity?.applicationContext?.let { it1 ->
                                viewModel.updateUserImage(userIdx ?: "",
                                    imageUri,
                                    it1)
                            }
                        }
                    }
                }
            }
    }


    //회원정보 UI에 입력하는 메서드
    private fun setUserDataUi(userEntity: UserEntity) {
        val ar_userAddr = userEntity.address!!.split("@")
        this.userEntity = userEntity
        //1)생년월일
        binding.tvUserBirthDateMypage.text = userEntity.birth_date
        //2)닉네임
        binding.tvNicknameMypage.text = userEntity.nickname
        //3)거주지역
        binding.tvAddrMypage.text = ar_userAddr[0]

        //프로필 이미지가 있을 때만 이미지 로딩
        if (userEntity.profile_ImgUrl?.isNotEmpty() == true) {
            //4)프로필 사진
            activity?.run {
                Glide.with(this.applicationContext)
                    .load(Constants.Base_img_url + userEntity.profile_ImgUrl)
                    .override(500, 500)
                    .into(binding.ivProfileImgMypage)
            }
        }


    }


    ///이미지 수정에 필요한 읽기, 쓰기 권한 세팅
    private fun setImagePermission(): PermissionListener {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val intent = Intent(Intent.ACTION_PICK)
                intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                intent.type = "image/*"
                //이미지 여러장 선택하기
                activityResultImage.launch(intent)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                return
            }
        }
        return permissionListener
    }

    //권한 체크 후 갤러리 실행
    private fun checkPermission(permissionListener: PermissionListener) {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE, //읽기
                Manifest.permission.WRITE_EXTERNAL_STORAGE, //쓰기
            ).check()
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
            MypageFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}


/*   //show, Hidden 이벤트
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        //화면이 보여질 때
        if (!hidden) {
        }
    }*/










