package com.example.sportsfriendrefac.presentation.login


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.databinding.FragmentLoginBinding
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.viewModel.LoginViewModel
import com.example.sportsfriendrefac.util.Constants
import kotlinx.coroutines.runBlocking
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFrag.newInstance] factory method to
 * create an instance of this fragment.
 */

class LoginFrag : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login),
    View.OnClickListener {

    val viewModel: LoginViewModel by activityViewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun init() {
        /*    runBlocking {
                App.instance.clearDataStore()
            }*/

        //자동로그인 체크
        if (viewModel.checkAutoLogin()) {
            //쉐어드에 저장된 UserId가 있는 경우 메인엑티비티로 이동
            val intent = Intent(activity?.applicationContext, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        (activity as LoginActivity?)?.supportActionBar?.hide()


        subscribeToLiveData()


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //1)로그인 버튼
            binding.btnLogin.id -> {
                val inputEmail = binding.edEmailLogin.text.toString()
                val inputPw = binding.edPwLogin.text.toString()

                //이메일 공백 검사
                if (inputEmail.isEmpty()) {
                    Toast.makeText(activity?.applicationContext, "이메일을 입력해주세요 ", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                //비밀번호 공백 검사
                else if (inputPw.isEmpty()) {
                    Toast.makeText(activity?.applicationContext,
                        "비밀번호를 입력해주세요 ",
                        Toast.LENGTH_SHORT)
                        .show()
                    return
                }


                //서버에 로그인 요청
                viewModel.loginCheck(UserEntity(
                    //서버에 전달할 회원가입에 필요한 정보
                    "",
                    "",
                    "",
                    "",
                    inputEmail,
                    inputPw,
                    "",
                    "",
                    ""))
            }

            //2)회원가입 버튼
            binding.btnRegister.id -> {
                //프래그먼트 이동시 전달할 데이터를 입력값에 넣기
                //nav_graph에서 미리 정해놓은 경로로 Fragment는 이동한다.
                //회원가입 프래그먼트로 이동
                val action =
                    LoginFragDirections.actionLoginToRegister()

                //Second 프래그먼트 전환
                findNavController().navigate(action)
            }
            else -> {
            }
        }


    }

    private fun subscribeToLiveData() {
        viewModel.liveUser.observe(viewLifecycleOwner) { event ->
            //이메일인증 클릭 시에만 실행
            event.getContentIfNotHandled()?.let {
                //이메일 검사
                if (it.trim() == "이메일불일치") {
                    Toast.makeText(App.instance.context(),
                        "이메일이 일치하지 않습니다.",
                        Toast.LENGTH_SHORT).show()
                    return@observe
                }
                //비밀번호 검사
                else if (it.trim() == "비밀번호불일치") {
                    Toast.makeText(App.instance.context(),
                        "비밀번호가 일치하지 않습니다.",
                        Toast.LENGTH_SHORT).show()
                    return@observe
                }

                //DataStore에 User정보 저장
                viewModel.saveUserData(it)

                //로그인 성공시 MainActivity로 이동
                val intent = Intent(activity?.applicationContext, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }
}

