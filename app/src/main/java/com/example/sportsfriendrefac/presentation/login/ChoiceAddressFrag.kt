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
import com.example.sportsfriendrefac.databinding.FragmentChoiceAddressBinding
import com.example.sportsfriendrefac.databinding.FragmentRegisterBinding
import com.example.sportsfriendrefac.presentation.bulletin.MainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChoiceAddressFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChoiceAddressFrag :
    BaseFragment<FragmentChoiceAddressBinding>(R.layout.fragment_choice_address),
    View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun init() {
        (activity as LoginActivity?)?.toolbarTitle?.text = "지역 선택"
        binding.tvInterestAddrChoice.setOnClickListener(this)
        binding.tvLiveAddrChoice.setOnClickListener(this)
        binding.btnCompleteAddr.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            //1)가입완료
            binding.btnCompleteAddr.id -> {

                val action =
                    ChoiceAddressFragDirections.actionChoiceAddressToLogin()
                //Second 프래그먼트 전환
                findNavController().navigate(action)
            }

            //2)거주지역 선택
            binding.tvLiveAddrChoice.id -> {


            }

            //3)관심지역 선택
            binding.tvInterestAddrChoice.id -> {


            }
            else -> {
            }
        }


    }


}