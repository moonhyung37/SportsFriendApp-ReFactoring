package com.example.sportsfriendrefac.base

import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel>
//입력값으로 레이아웃 경로를 입력
    (@LayoutRes private val layoutResId: Int) : AppCompatActivity() {
    private lateinit var binding: T
    protected abstract val viewModel: V
    abstract val TAG: String // 액티비티 태그


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        //엑티비티마다 다른 레이아웃 Id를 입력값으로 받ㄴ
        binding = DataBindingUtil.setContentView(this, layoutResId)
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart")
    }


    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }


}