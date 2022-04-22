package com.example.sportsfriendrefac.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.databinding.FragmentChoiceAddressBinding
import timber.log.Timber

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
    var user: User? = null
    var fragmentDialog: DaumAddrFragDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun init() {
        //프래그먼트 타이틀
        (activity as LoginActivity?)?.toolbarTitle?.text = "지역 선택"

        fragmentDialog = DaumAddrFragDialog()
        binding.tvInterestAddrChoice.setOnClickListener(this)
        binding.tvLiveAddrChoice.setOnClickListener(this)
        binding.btnCompleteAddr.setOnClickListener(this)

        //라이브데이터 옵저빙
        subscribeToLiveData()

        //입력한 회원정보를 args로 받고 객체에 담음
        val argsUser: ChoiceAddressFragArgs by navArgs()
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


        //다이얼로그에서 주소 클릭 시 실행
        //-다이얼로그안에 다음 주소선택 웹뷰가 있음
        fragmentDialog?.setAddressClickListener(object : DaumAddrFragDialog.OnclickListener {
            override fun onButtonClick(address: String?, flagAddr: Int) {
                if (address != null) {
                    //1번 거주지역
                    if (flagAddr == 1) {
                        binding.tvLiveAddrChoice.text = address
                    }
                    //2번 관심지역
                    if (flagAddr == 2) {
                        binding.tvInterestAddrChoice.text = address
                    }

                }

                Timber.d("주소: $address")
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            //1)가입완료 버튼
            binding.btnCompleteAddr.id -> {
                //선택한 지역
                val liveAddr = binding.tvLiveAddrChoice.text.toString()
                val interestAddr = binding.tvInterestAddrChoice.text.toString()


                //관심지역(선택사항)을 입력하지 않은 경우 구분
                if (interestAddr.isEmpty()) {
                    user?.address = liveAddr
                } else {
                    user?.address = "$liveAddr$$interestAddr"
                }

                //서버에 회원가입 요청
                (activity as LoginActivity?)?.viewModel?.requestRegisterUser(User(
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

            //2)거주지역 선택
            binding.tvLiveAddrChoice.id -> {

                (activity as LoginActivity?)?.supportFragmentManager?.let {
                    binding.tvLiveAddrChoice.text = ""
                    fragmentDialog?.showDialog(it, "LiveAddr", 1)
                }

            }

            //3)관심지역 선택
            binding.tvInterestAddrChoice.id -> {
                (activity as LoginActivity?)?.supportFragmentManager?.let {
                    binding.tvInterestAddrChoice.text = ""
                    fragmentDialog?.showDialog(it, "InterestAddr", 2)
                }
            }
            else -> {
            }
        }


    }

    private fun subscribeToLiveData() {
        (activity as LoginActivity?)?.viewModel?.liveRegister?.observe(this) {
            //화면전환
            val action =
                ChoiceAddressFragDirections.actionChoiceAddressToLogin()
            findNavController().navigate(action)
        }
    }


}