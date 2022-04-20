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
import com.example.sportsfriendrefac.databinding.FragmentLoginBinding
import com.example.sportsfriendrefac.databinding.FragmentRegisterBinding
import com.example.sportsfriendrefac.presentation.bulletin.MainActivity

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

                val action =
                    RegisterFragDirections.actionRegisterToCertifiedEmail()
                //Second 프래그먼트 전환
                findNavController().navigate(action)
            }
            else -> {
            }
        }


    }

}