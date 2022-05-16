package com.example.sportsfriendrefac.presentation.bulletin


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.sportsfriendref.BulletinRvAdapter
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.databinding.ActivityEditBulletinBinding
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.adapter.BulletinImageAdapter
import com.example.sportsfriendrefac.presentation.login.DaumAddrFragDialog
import com.example.sportsfriendrefac.util.Constants
import org.json.JSONException
import org.json.JSONObject

/* 모집 글 수정 엑티비티 */
class EditBulletinActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var imageResultLauncher: ActivityResultLauncher<Intent>
    private var daumFragmentDialog: DaumAddrFragDialog? = null
    private var choiceExerFragDialog: ChoiceExerFragDialog? = null
    var rv_Adapter_img: BulletinImageAdapter? = null
    var list_imgUri: ArrayList<Uri>? = null


    private lateinit var binding: ActivityEditBulletinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_bulletin)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_bulletin)
        //버튼클릭 리스너 등록
        binding.ivBackiconEditBullein.setOnClickListener(this)
        binding.edAddrEditBulletin.setOnClickListener(this)
        binding.edExerEditBulletin.setOnClickListener(this)
        binding.btnImageEditBulletin.setOnClickListener(this)
        binding.btnEditBulletin.setOnClickListener(this)

        //프래그먼트 다이얼로그
        daumFragmentDialog = DaumAddrFragDialog()
        choiceExerFragDialog = ChoiceExerFragDialog()
        //프래그먼트 다이얼로그 세팅
        setFragmentDialog()
        //이미지 선택 기능 세팅
        setImageChoice()

        //기존에 입력한 모집글 정보 View에 입력
        if (intent.hasExtra("1")) {
            binding.edTitleEditBulletin.setText(intent.getStringExtra("1"))//모집글 제목
            binding.edContentEditBulletin.setText(intent.getStringExtra("2"))//모집글 내용
            binding.edAddrEditBulletin.setText(intent.getStringExtra("3"))//관심지역
            binding.edExerEditBulletin.setText(intent.getStringExtra("4"))//관심운동
            parseJsonImageAddRv(intent.getStringExtra("5").toString()) //모집 글 이미지 리사이클러뷰에 입력
        }
    }


    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 모집글 수정
            binding.btnEditBulletin.id -> {
                //선택한 이미지 uri 문자열로 변환 ("@")
                var bltn_img_Url = ""

                //이미지가 있을 때만 문자열 "@" 넣기
                if (list_imgUri?.size != 0) {
                    list_imgUri?.map {
                        bltn_img_Url += "$it@"
                    }
                    bltn_img_Url = bltn_img_Url.substring(0, bltn_img_Url.length - 1)
                }

                val intent =
                    Intent(applicationContext, MainActivity::class.java).apply {
                        putExtra("1", binding.edTitleEditBulletin.text.toString()) //제목
                        putExtra("2", binding.edContentEditBulletin.text.toString()) //내용
                        putExtra("3", binding.edAddrEditBulletin.text.toString()) //관심지역
                        putExtra("4", binding.edExerEditBulletin.text.toString()) //관심운동
                        putExtra("5", bltn_img_Url) //모집 글 이미지(다중이미지)
                    }

                setResult(RESULT_OK, intent)
                if (!isFinishing) finish()
            }
            //2) 뒤로가기
            binding.ivBackiconEditBullein.id -> {
                if (!isFinishing) finish()
            }

            //3) 주소검색
            binding.edAddrEditBulletin.id -> {
                daumFragmentDialog?.showDialog(supportFragmentManager, "LiveAddr", 1)
            }
            //4) 관심운동추가
            binding.edExerEditBulletin.id -> {
                //관심운동 추가 다이얼로그 뛰우기
                choiceExerFragDialog?.showChoiceExerFragDialog(supportFragmentManager, "ChoiceExer")

            }
            //5) 이미지 선택
            binding.btnImageEditBulletin.id -> {
                //갤러리에서 이미지 갖고오기
                val intent = Intent(Intent.ACTION_PICK)
                intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                intent.type = "image/*"
                //이미지 여러장 선택하기
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                imageResultLauncher.launch(intent)
            }
            else -> {
            }
        }
    }

    private fun setImageChoice() {
        //리사이클러뷰에 사용할 이미지 리스트
        list_imgUri = ArrayList()
        //이미지 리사이클러뷰 어댑터 세팅
        rv_Adapter_img = BulletinImageAdapter(list_imgUri!!, applicationContext)
        binding.rvEditBulletin.adapter = rv_Adapter_img

        //이미지 수정시 수정한 이미지 URI 갖고오기
        imageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //엑티비티에서 데이터를 갖고왔을 때만 실행
                if (it.resultCode == RESULT_OK) {
                    //1)여러장 선택
                    if (it.data?.clipData != null) {
                        //선택된 이미지의 개수를 변수에 저장
                        val count = it.data!!.clipData!!.itemCount
                        //최대 5장 까지
                        if (count > 5) {
                            Toast.makeText(
                                applicationContext,
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

    //모집 글 이미지 리사이클러뷰에 추가
    private fun parseJsonImageAddRv(loadData_json: String) {
        //이미지 URL이 있는 경우에만 실행
        if (loadData_json.isNotEmpty() && loadData_json != "null") {
            try {
                val jsonObject = JSONObject(loadData_json)
                //php에서 저장할 때 jsonArray의 키
                val jsonArray = jsonObject.getJSONArray("json_imgUri")
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    list_imgUri?.add(Uri.parse(Constants.Base_img_url + item.getString("image$i")))
                }
                rv_Adapter_img?.notifyDataSetChanged()
            } catch (e: JSONException) {
                //서버에서 받아온 Json 형식이 아닌 경우

            }
        }
    }


    private fun setFragmentDialog() {
        //1)선택한 주소 프래그먼트 다이얼로그에서 받기
        daumFragmentDialog?.setAddressClickListener(object :
            DaumAddrFragDialog.OnclickListener {
            override fun onButtonClick(address: String?, flagAddr: Int) {
                if (address != null) {
                    //1번 거주지역
                    if (flagAddr == 1) {
                        binding.edAddrEditBulletin.setText(address)
                    }
                }
            }
        })

        //2)선택한 관심운동을 프래그먼트 다이얼로그에서 받기
        choiceExerFragDialog?.setChoiceExerFragListener(object :
            ChoiceExerFragDialog.OnclickListener {
            override fun onButtonClick(choiceExer: String, flag: String) {
                binding.edExerEditBulletin.setText(choiceExer)
            }
        })
    }
}