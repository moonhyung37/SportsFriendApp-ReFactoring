package com.example.sportsfriendrefac.presentation.login

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.databinding.FragmentRegisterBinding
import com.example.sportsfriendrefac.presentation.viewModel.LoginViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFrag :
    BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register),
    View.OnClickListener {
    val user: User? = null
    private var minPw: Int? = null  //비밀번호 최소 자릿수
    private var maxPw: Int? = null  //비밀번호 최대 자릿수
    val viewModel: LoginViewModel by activityViewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun init() {
        //다음버튼
        binding.btnNextRegister.setOnClickListener(this)
        //비밀번호 보기 버튼
        binding.ivSearchPwRegister.setOnClickListener(this)
        (activity as LoginActivity?)?.supportActionBar?.show()
        (activity as LoginActivity?)?.toolbarTitle?.text = "회원정보 입력"
        subscribeToLiveData()

        //비밀번호 자릿수
        minPw = 8
        maxPw = 15
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            //다음 버튼
            binding.btnNextRegister.id -> {

                //이메일 유효성 검사
                if (!checkEmailRegex(binding.edEmailRegister.text.toString())) {
                    binding.tvEmailCheckRegister.text = "올바른 이메일 형식을 입력해주세요"
                    binding.tvCheckPw1Message.text = ""
                    binding.tvCheckNicknameJoin.text = ""
                    binding.tvCheckBirthDate.text = ""
                    return
                }


                //비밀번호 유효성 검사사
                else if (!checkPwRegex(binding.edPwRegister.text.toString(), minPw!!, maxPw!!)) {
                    binding.tvCheckPw1Message.text = "영문, 숫자, 특수문자 포함 8자리 이상 입력해주세요"
                    binding.tvEmailCheckRegister.text = ""
                    binding.tvCheckNicknameJoin.text = ""
                    binding.tvCheckBirthDate.text = ""
                    return
                }

                //닉네임 유효성 검사
                //2자리이상
                else if (binding.edNicknameRegister.text.length <= 2) {
                    binding.tvCheckNicknameJoin.text = "2자리이상 입력해주세요"
                    binding.tvCheckPw1Message.text = ""
                    binding.tvEmailCheckRegister.text = ""
                    binding.tvCheckBirthDate.text = ""
                    return
                }

                //생년월일 검사
                //2자리이상
                else if (binding.edUserBirthDateRegister.text.length != 6) {
                    binding.tvCheckBirthDate.text = "올바른 형식으로 입력해주세요(ex 970923)"
                    binding.tvCheckPw1Message.text = ""
                    binding.tvEmailCheckRegister.text = ""
                    binding.tvCheckNicknameJoin.text = ""
                    return
                }

                //코루틴으로 실행
                viewModel.redundancyCheck(binding.edEmailRegister.text.toString())
            }

            //비밀번호 보기
            binding.ivSearchPwRegister.id -> {
                if (binding.edPwRegister.inputType == 129) {
                    binding.edPwRegister.inputType = InputType.TYPE_CLASS_TEXT
                } else {
                    binding.edPwRegister.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

                }
            }

            else -> {
            }
        }


    }

    private fun subscribeToLiveData() {

        //이메일 중복검사
        viewModel.liveUser.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                //이메일 중복
                if (it.trim() == "중복") {
                    binding.tvEmailCheckRegister.text = "중복된 이메일 입니다."
                    binding.tvCheckBirthDate.text = ""
                    binding.tvCheckPw1Message.text = ""
                    binding.tvCheckNicknameJoin.text = ""
                    return@let
                }

                //입력한 회원정보
                val email = binding.edEmailRegister.text.toString()
                val password = binding.edPwRegister.text.toString()
                val nickname = binding.edNicknameRegister.text.toString()
                val birthDate = binding.edUserBirthDateRegister.text.toString()

                //프래그먼트 이동 시 회원정보 전달
                val action =
                    RegisterFragDirections.actionRegisterToCertifiedEmail(
                        email,
                        password,
                        nickname,
                        birthDate,
                    )

                //이메일인증 프레그먼트로 이동
                findNavController().navigate(action)
            }
        }
    }

    private fun checkPwRegex(pw_check: String?, min: Int, max: Int): Boolean {
        //비밀번호 정규식 (문자, 숫자, 특수문자 포함 8자 ~ 15자 이상)
        val reg_pw = Regex("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{$min,$max}.\$")

        //정규식 검사
        if (!pw_check.toString().matches(reg_pw)) {
            //일치하지 않는 경우 false
            return false
        }
        //일치하는 경우 true
        return true
    }

    //email 정규식 검사 함수
    private fun checkEmailRegex(email: String?): Boolean {
        //Email 정규식
        val reg_email =
            Regex("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        //정규식 검사
        if (!email.toString().matches(reg_email)) {
            //일치하지 않는 경우 false
            return false
        }
        //일치하는 경우 true
        return true
    }
}