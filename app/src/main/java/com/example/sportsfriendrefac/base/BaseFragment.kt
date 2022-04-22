package com.example.sportsfriendrefac.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import timber.log.Timber

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes val layoutId: Int,
) : Fragment() {
    lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        //프래그먼트에 필요한 세팅을 하는 메서드
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    //프래그먼트마다 상세한 세팅코드가 다르기 때문에 작성함.
    //각각의 프래그먼트마다 Override 되는 init()가 실행된다.
    //override한 부분에 세팅할 코드를 작성하면된다.
    //onCreatedView에서 실행
    abstract fun init()

    protected fun showToast(msg: String) =
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}