package com.example.sportsfriendrefac.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.databinding.FragmentCertifiedEmailBinding
import com.example.sportsfriendrefac.databinding.FragmentRegisterBinding
import com.example.sportsfriendrefac.presentation.viewModel.LoginViewModel
import timber.log.Timber

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
    val viewModel: LoginViewModel by activityViewModels<LoginViewModel>()

    var user: User? = null


    //회원가입 확인 플래그
    // - 라이브데이터에 옵저빙 되는 것 방지
    var emailNum: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init() {
        //라이브데이터 구독(이메일 인증)
        subscribeToLiveData()
        //액션바 타이틀 제목
        (activity as LoginActivity?)?.toolbarTitle?.text = "이메일 인증"
        binding.btnNextEmail.setOnClickListener(this)
        binding.btnReceiveEmail.setOnClickListener(this)
        val argsUser: CertifiedEmailFragArgs by navArgs()
        user = User(
            "",
            "",
            "",
            argsUser.nickname,
            argsUser.email,
            argsUser.password,
            "",
            argsUser.birthDate,
            "")

        //입력한 이메일 입력
        binding.tvEmail.text = user?.email


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //1)다음(지역 선택으로 이동)
            binding.btnNextEmail.id,
            -> {
                if (emailNum == binding.edCertifiedNumberEmail.text.toString()) {
                    val action =
                        CertifiedEmailFragDirections.actionCertifiedEmailToChoiceAddress(
                            user!!.email,
                            user!!.password,
                            user!!.nickname,
                            user!!.birth_date
                        )
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(activity?.applicationContext,
                        "인증번호가 일치하지 않습니다.",
                        Toast.LENGTH_SHORT).show()
                }


                /*    val action =
                        CertifiedEmailFragDirections.actionCertifiedEmailToChoiceAddress(
                            user!!.email,
                            user!!.password,
                            user!!.nickname,
                            user!!.birth_date
                        )
                    //프레그먼트 전환
                    findNavController().navigate(action)*/

            }

            //2)인증번호 보내기
            binding.btnReceiveEmail.id -> {
                //서버에 회원가입 요청
                viewModel.certifiedEmail(User(
                    //서버에 전달할 회원가입에 필요한 정보
                    "",
                    "",
                    "",
                    user!!.nickname,
                    user!!.email,
                    user!!.password,
                    user!!.address,
                    user!!.birth_date,
                    ""))
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