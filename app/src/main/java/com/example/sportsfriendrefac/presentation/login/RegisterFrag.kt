package com.example.sportsfriendrefac.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.databinding.FragmentLoginBinding
import com.example.sportsfriendrefac.databinding.FragmentRegisterBinding
import com.example.sportsfriendrefac.presentation.bulletin.MainActivity
import timber.log.Timber

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun init() {
        binding.btnNextRegister.setOnClickListener(this)
        (activity as LoginActivity?)?.supportActionBar?.show()
        (activity as LoginActivity?)?.toolbarTitle?.text = "회원정보 입력"
    }

    override fun onClick(v: View?) {
        when (v?.id) {


            binding.btnNextRegister.id -> {
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
            else -> {
            }
        }


    }

}