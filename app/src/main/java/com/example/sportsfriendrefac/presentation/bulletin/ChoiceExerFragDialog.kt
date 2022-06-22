package com.example.sportsfriendrefac.presentation.bulletin


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.databinding.FragmentDialogChoiceExerBinding


/* 모집 글 추가, 수정 프래그먼트 다이얼로그*/
class ChoiceExerFragDialog : DialogFragment(), View.OnClickListener {
    private var fragmentInterfaceChoiceExer: OnclickListener? = null
    private var _binding: FragmentDialogChoiceExerBinding? = null
    private val binding get() = _binding!!

    var List_Cb: ArrayList<CheckBox>? = null   //체크된 박스를 담는 리스트
    var check_exer_str: String? = null
    var check_count = 0   //체크된 박스의 수

    //다이얼로그에서 선택한 주소값을 받아오기 위해 작성
    interface OnclickListener {
        //선택한 운동
        fun onButtonClick(choiceExer: String, flag: String)
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
        _binding = FragmentDialogChoiceExerBinding.inflate(inflater, container, false)
        //버튼클릭리스너 세팅
        binding.ivBackiconEditMyapge.setOnClickListener(this) //뒤로가기
        List_Cb = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        check_count = 0
        List_Cb?.clear()
        //관심운동을 체크박스에 담는다.
        List_Cb?.add(binding.chb1)
        List_Cb?.add(binding.chb2)
        List_Cb?.add(binding.chb3)
        List_Cb?.add(binding.chb4)
        List_Cb?.add(binding.chb5)
        List_Cb?.add(binding.chb6)
        List_Cb?.add(binding.chb7)
        List_Cb?.add(binding.chb8)
        List_Cb?.add(binding.chb9)
        List_Cb?.add(binding.chb10)
        List_Cb?.add(binding.chb11)
        List_Cb?.add(binding.chb12)
        List_Cb?.add(binding.chb13)
        List_Cb?.add(binding.chb14)
        binding.ivBackiconEditMyapge.setOnClickListener(this)
        binding.btnCompleteAddr.setOnClickListener(this)


        //전체 체크박스 개수만큼 반복
        List_Cb?.map {
            it.setOnCheckedChangeListener { _, isChecked ->
                //체크된 경우
                if (isChecked) {
                    //2개 이상 체크된 경우
                    if (check_count == 1) {
                        //체크기능 끄기
                        it.isChecked = false
                        //n개 까지만 체크 허용
                        Toast.makeText(
                            activity?.applicationContext,
                            "최대 1개 까지만 선택 가능합니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnCheckedChangeListener
                    }
                    check_count++ //1증가
                }

                //체크를 해제한 경우
                else {
                    check_count-- //1감소
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        check_exer_str = null
        List_Cb = null
    }

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1)선택완료
            binding.btnCompleteAddr.id -> {
                check_exer_str = ""
                //체크된 박스를 찾는다.
                List_Cb?.filter {
                    it.isChecked
                }?.map {
                    //체크된 박스의 텍스트를 갖고오기
                    check_exer_str = it.text.toString()
                }
                fragmentInterfaceChoiceExer?.onButtonClick(check_exer_str ?: "", "1")
                dismiss()
            }
            //2)뒤로가기
            binding.ivBackiconEditMyapge.id -> {
                dismiss()
            }

            else -> {
            }
        }
    }


    fun setChoiceExerFragListener(fragmentInterfacer: OnclickListener) {
        this.fragmentInterfaceChoiceExer = fragmentInterfacer
    }

    fun showChoiceExerFragDialog(manager: FragmentManager, TAG: String) {
        show(manager, TAG)
    }

}

