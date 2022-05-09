package com.example.sportsfriendrefac.presentation.login

import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.databinding.FragmentCertifiedEmailBinding
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.presentation.viewModel.UserViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CertifiedEmailFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class CertifiedEmailFrag() :
    BaseFragment<FragmentCertifiedEmailBinding>(R.layout.fragment_certified_email),
    View.OnClickListener {
    val viewModel: UserViewModel by activityViewModels<UserViewModel>()

    var userEntity: UserEntity? = null


    //회원가입 확인 플래그
    // - 라이브데이터에 옵저빙 되는 것 방지
    var emailNum: String? = null

    override fun init() {
        //라이브데이터 구독(이메일 인증)
        subscribeToLiveData()
        //액션바 타이틀 제목
        (activity as LoginActivity?)?.toolbarTitle?.text = "이메일 인증"
        binding.btnNextEmail.setOnClickListener(this)
        binding.btnReceiveEmail.setOnClickListener(this)
        val argsUser: CertifiedEmailFragArgs by navArgs()

        //회원가입시 입력한 정보
        userEntity = UserEntity(
            "",
            "",
            "",
            argsUser.nickname,
            argsUser.email,
            argsUser.password,
            "",
            argsUser.birthDate,
            "",
            "")

        //입력한 이메일 텍스트뷰에 보여주기
        binding.tvEmail.text = argsUser.email
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //1)다음(지역 선택으로 이동)
            binding.btnNextEmail.id,
            -> {
                //이메일 인증번호 검사
                if (emailNum == binding.edCertifiedNumberEmail.text.toString()) {

                    //성공 -> 주소검색 프래그먼트로 이동
                    val action =
                        CertifiedEmailFragDirections.actionCertifiedEmailToChoiceAddress(
                            userEntity?.email ?: "",
                            userEntity?.password ?: "",
                            userEntity?.nickname ?: "",
                            userEntity?.birth_date ?: ""
                        )
                    findNavController().navigate(action)
                } else {
                    //실패 -
                    Toast.makeText(activity?.applicationContext,
                        "인증번호가 일치하지 않습니다.",
                        Toast.LENGTH_SHORT).show()
                }
            }

            //2)인증번호 받기
            binding.btnReceiveEmail.id -> {
                //서버에 인증번호 요청
                viewModel.certifiedEmail(
                    //입력한 이메일
                    binding.tvEmail.text.toString()
                )
            }

            else -> {
            }
        }


    }

    private fun subscribeToLiveData() {
        viewModel.liveUser.observe(viewLifecycleOwner) { event ->
            //이메일인증 클릭 시에만 실행
            event.getContentIfNotHandled()?.let {
                emailNum = it
                binding.tvSendCheck.text = "인증번호를 전송했습니다"
            }
        }
    }
}