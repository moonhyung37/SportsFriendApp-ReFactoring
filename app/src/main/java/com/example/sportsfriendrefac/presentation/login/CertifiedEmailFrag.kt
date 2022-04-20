package com.example.sportsfriendrefac.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.databinding.FragmentCertifiedEmailBinding
import com.example.sportsfriendrefac.databinding.FragmentRegisterBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CertifiedEmailFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class CertifiedEmailFrag :
    BaseFragment<FragmentCertifiedEmailBinding>(R.layout.fragment_certified_email),
    View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init() {
        (activity as LoginActivity?)?.toolbarTitle?.text = "이메일 인증"
        binding.btnNextEmail.setOnClickListener(this)
        binding.btnReceiveEmail.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            //1)거주지역 선택으로 이동
            binding.btnNextEmail.id -> {
                val action =
                    CertifiedEmailFragDirections.actionCertifiedEmailToChoiceAddress()
                //Second 프래그먼트 전환
                findNavController().navigate(action)
            }

            //2)인증번호 보내기
            binding.btnReceiveEmail.id -> {


            }


            else -> {
            }
        }


    }

}