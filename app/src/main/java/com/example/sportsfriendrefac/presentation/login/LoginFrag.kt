package com.example.sportsfriendrefac.presentation.login


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.databinding.FragmentLoginBinding
import com.example.sportsfriendrefac.presentation.bulletin.MainActivity
import timber.log.Timber


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFrag.newInstance] factory method to
 * create an instance of this fragment.
 */

class LoginFrag : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login),
    View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun init() {
        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        (activity as LoginActivity?)?.supportActionBar?.hide()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //1)로그인 버튼
            binding.btnLogin.id -> {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
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
}


/*class LoginFrag : Fragment() {


  /*  override fun onCreateView(
          inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?,
      ): View? {
          // Inflate the layout for this fragment
          return inflater.inflate(R.layout.fragment_login, container, false)
      }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


}*/


/*class LoginFrag : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login),
    View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
*//*
    override fun onClick(v: View?) {
        when (v?.id) {
            //1)로그인 버튼
            binding.btnLogin.id -> {
                *//**//*    val intent = Intent(, 이동하려는엑티비티::class.java)
                    startActivity(intent)
                    finish()*//**//*
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
    }*//*
}*/



