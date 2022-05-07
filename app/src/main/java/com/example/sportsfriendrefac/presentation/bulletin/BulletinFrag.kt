package com.example.sportsfriendrefac.presentation.bulletin

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseFragment
import com.example.sportsfriendrefac.databinding.FragmentBulletinBinding
import com.example.sportsfriendrefac.presentation.adapter.BulletinRvAdapter
import com.example.sportsfriendrefac.presentation.viewModel.MainViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BulletinFrag : BaseFragment<FragmentBulletinBinding>(R.layout.fragment_bulletin) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: MainViewModel by activityViewModels<MainViewModel>()

    private val bulletinRvAdapter: BulletinRvAdapter by lazy {
        BulletinRvAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun init() {
        //모집 글 정보 리스트 조회하기
        viewModel.selectAllBulletin()

        //리사이클러뷰 세팅
        binding.rvBulletinRv.apply {
            //Context 받아오기
            activity?.applicationContext?.run { bulletinRvAdapter.setContext(this) }
            adapter = bulletinRvAdapter
        }

        viewModel.live_bulletin.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled().run {
                //모집 글 리사이클러뷰에 데이터 입력
                this?.let { it1 -> bulletinRvAdapter.setData(it1) }
            }
        }
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

/* //show, Hidden 이벤트
   override fun onHiddenChanged(hidden: Boolean) {
       super.onHiddenChanged(hidden)
       if (!hidden) {
       }
   }*/