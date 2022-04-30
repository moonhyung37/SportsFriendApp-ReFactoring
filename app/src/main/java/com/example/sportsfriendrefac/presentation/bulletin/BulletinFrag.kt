package com.example.sportsfriendrefac.presentation.bulletin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.databinding.FragmentBulletinBinding
import com.example.sportsfriendrefac.databinding.FragmentCertifiedEmailBinding
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.login.LoginActivity
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BulletinFrag : BaseFragment<FragmentBulletinBinding>(R.layout.fragment_bulletin) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun init() {
        //프래그먼트 타이틀
        (activity as MainActivity?)?.toolbarTitle?.text = param1
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            //기본 생성자 생성
            BulletinFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}