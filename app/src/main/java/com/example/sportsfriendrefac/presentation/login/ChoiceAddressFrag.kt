package com.example.sportsfriendrefac.presentation.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.databinding.FragmentChoiceAddressBinding
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.presentation.viewModel.LoginViewModel
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
    private var user: User? = null
    private var fragmentDialog: DaumAddrFragDialog? = null
    private val viewModel: LoginViewModel by activityViewModels<LoginViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun init() {
        //라이브데이터 옵저빙
        subscribeToLiveData()

        //프래그먼트 타이틀
        (activity as LoginActivity?)?.toolbarTitle?.text = "지역 선택"
        //다이얼로그 객체 생성
        fragmentDialog = DaumAddrFragDialog()
        binding.tvInterestAddrChoice.setOnClickListener(this)
        binding.tvLiveAddrChoice.setOnClickListener(this)
        binding.btnCompleteAddr.setOnClickListener(this)


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


                //거주지역을 입력하지 않은 경우(검사)
                if (liveAddr.isEmpty()) {
                    Toast.makeText(activity?.applicationContext, "거주지역을 선택해주세요", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                //관심지역(선택사항)을 입력하지 않은 경우 구분
                if (interestAddr.isEmpty()) {
                    user?.address = liveAddr
                } else {
                    user?.address = "$liveAddr$$interestAddr"
                }

                //서버에 회원가입 요청
                viewModel.requestRegisterUser(UserEntity(
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
                //주소검색 다이얼로그 뛰우기
                (activity as LoginActivity?)?.supportFragmentManager?.let {
                    binding.tvLiveAddrChoice.text = ""
                    fragmentDialog?.showDialog(it, "LiveAddr", 1)
                }

            }

            //3)관심지역 선택
            binding.tvInterestAddrChoice.id -> {
                //주소검색 다이얼로그 뛰우기
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
        //로그인 화면으로 이동
        viewModel.liveUser.observe(viewLifecycleOwner) { event ->
            Timber.d("1111")
            event.getContentIfNotHandled()?.let {
                Toast.makeText(activity?.applicationContext, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT)
                    .show()
                val action =
                    ChoiceAddressFragDirections.actionChoiceAddressToLogin()
                findNavController().navigate(action)
            }
        }
    }
}