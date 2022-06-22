package com.example.sportsfriendrefac.presentation.bulletin


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.databinding.FragmentDialogAddBulletinBinding
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.adapter.BulletinImageAdapter
import com.example.sportsfriendrefac.presentation.login.DaumAddrFragDialog


/* 모집 글 추가, 수정 프래그먼트 다이얼로그*/
class AddBulletinFragDialog : DialogFragment(), View.OnClickListener {
    private var fragmentInterfacerBulletin: OnclickListener? = null
    private var daumFragmentDialog: DaumAddrFragDialog? = null
    private var choiceExerFragDialog: ChoiceExerFragDialog? = null


    var rv_Adapter_img: BulletinImageAdapter? = null
    var list_imgUri: ArrayList<Uri>? = null
    private var _binding: FragmentDialogAddBulletinBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageResultLauncher: ActivityResultLauncher<Intent>

    //다이얼로그에서 선택한 주소값을 받아오기 위해 작성
    interface OnclickListener {
        //flag 1번: 추가 2번: 수정
        fun onButtonClick(bulletinEntity: BulletinEntity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //전체화면 설정
        setStyle(STYLE_NO_TITLE, R.style.AddBulletin_fullscreen)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDialogAddBulletinBinding.inflate(inflater, container, false)
        daumFragmentDialog = DaumAddrFragDialog()
        choiceExerFragDialog = ChoiceExerFragDialog()

        //버튼클릭리스너 세팅
        binding.ivBackiconAddBullein.setOnClickListener(this)
        binding.btnAddBulletin.setOnClickListener(this)
        binding.btnImageAddBulletin.setOnClickListener(this)
        binding.edExerAddBulletin.setOnClickListener(this)
        binding.edAddrAddBulletin.setOnClickListener(this)


        //프래그먼트 다이얼로그 세팅
        setFragmentDialog()
        //이미지 선택 기능 세팅
        setImageChoice()
        return binding.root
    }

    private fun setImageChoice() {
        //이미지 처리 리스트
        list_imgUri = ArrayList()
        //이미지 리사이클러뷰 어댑터 세팅
        rv_Adapter_img =
            activity?.let { BulletinImageAdapter(list_imgUri!!, it.applicationContext) }
        binding.rvAddBulletin.adapter = rv_Adapter_img


        imageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //엑티비티에서 데이터를 갖고왔을 때만 실행
                if (it.resultCode == RESULT_OK) {
                    //1)여러장 선택
                    if (it.data?.clipData != null) {
                        //선택된 이미지의 개수를 변수에 저장
                        val count = it.data!!.clipData!!.itemCount
                        //최대 n장 까지
                        if (count > 5) {
                            Toast.makeText(
                                activity?.applicationContext,
                                "사진은 5장까지 선택 가능합니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@registerForActivityResult
                        }

                        //기존에 선택된 이미지 초기화
                        list_imgUri?.clear()
                        //선택한 이미지의 개수 만큼 반복
                        for (i in 0 until count) {
                            //선택한 이미지의 개수만큼 이미지의 uri를 추출해서 리스트에 저장
                            val imageUri = it.data?.clipData!!.getItemAt(i).uri
                            list_imgUri?.add(imageUri)
                        }

                        //2)단일 선택
                    } else {
                        it.data?.data?.let { uri ->
                            //이미지 uri
                            val imageUri: Uri? = it.data?.data
                            //이미지 uri가 null 이 아닐때만 리스트에 추가.
                            if (imageUri != null) {
                                list_imgUri?.add(imageUri)
                            }
                        }
                    }
                    rv_Adapter_img?.notifyDataSetChanged()
                }
            }
    }

    private fun setFragmentDialog() {
        //선택한 주소 프래그먼트 다이얼로그에서 받기
        daumFragmentDialog?.setAddressClickListener(object : DaumAddrFragDialog.OnclickListener {
            override fun onButtonClick(address: String?, flagAddr: Int) {
                if (address != null) {
                    //1번 거주지역
                    if (flagAddr == 1) {
                        binding.edAddrAddBulletin.setText(address)
                    }
                }
            }
        })

        //선택한 관심운동을 프래그먼트 다이얼로그에서 받기
        choiceExerFragDialog?.setChoiceExerFragListener(object :
            ChoiceExerFragDialog.OnclickListener {
            override fun onButtonClick(choiceExer: String, flag: String) {
                binding.edExerAddBulletin.setText(choiceExer)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        daumFragmentDialog = null
        choiceExerFragDialog = null
        _binding = null
        list_imgUri = null
        rv_Adapter_img = null

    }

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 모집 글 작성 버튼
            binding.btnAddBulletin.id -> {
                val userIdx = (activity as MainActivity).userIdx
                //선택한 이미지 uri 문자열로 변환
                var bltn_img_Url = ""

                //이미지가 있을 때만
                if (list_imgUri?.size != 0) {
                    list_imgUri?.map {
                        bltn_img_Url += "$it@"
                    }

                    //마지막 문자열 자르기 ("@")
                    bltn_img_Url = bltn_img_Url.substring(0, bltn_img_Url.length - 1)
                }

                val bulletinEntity = BulletinEntity(
                    "",
                    "",
                    userIdx ?: "",
                    binding.edTitleAddBulletin.text.toString(),
                    binding.edContentAddBulletin.text.toString(),
                    bltn_img_Url,
                    binding.edExerAddBulletin.text.toString(),
                    binding.edAddrAddBulletin.text.toString(),
                    1,
                    "0"
                )

                //작성한 모집글 정보 보내기
                fragmentInterfacerBulletin?.onButtonClick(bulletinEntity)
                dismiss()
            }
            //2) 뒤록가기 버튼
            binding.ivBackiconAddBullein.id -> {
                dismiss()
            }
            //3) 이미지 추가 버튼
            binding.btnImageAddBulletin.id -> {
                //이미지의 URI를 intent를 사용해 가져오는 코드
                val intent = Intent(Intent.ACTION_PICK)
                intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                intent.type = "image/*"
                //이미지 여러장 선택하기
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                //엑티비티이동
                imageResultLauncher.launch(intent)
            }
            //4) 관심운동 추가 버튼
            binding.edExerAddBulletin.id -> {
                //관심운동 추가 다이얼로그 뛰우기
                (activity as MainActivity?)?.supportFragmentManager?.let {
                    choiceExerFragDialog?.showChoiceExerFragDialog(it, "ChoiceExer")
                }
            }
            //5) 지역 추가 버튼
            binding.edAddrAddBulletin.id -> {
                //주소검색 다이얼로그 뛰우기
                (activity as MainActivity?)?.supportFragmentManager?.let {
                    daumFragmentDialog?.showDialog(it, "LiveAddr", 1)
                }

            }
            else -> {
            }
        }
    }


    fun setBulletinClickListener(fragmentInterfacer: OnclickListener) {
        this.fragmentInterfacerBulletin = fragmentInterfacer
    }

    //flag 1번: 추가
    //flag 2번: 수정
    fun showAddBulletinDialog(manager: FragmentManager, TAG: String) {
        show(manager, TAG)
    }

}








