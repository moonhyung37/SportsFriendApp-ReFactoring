package com.example.sportsfriendrefac.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import timber.log.Timber

//1)ViewBinding을 상속하는 객체만 받는다.
//2)
abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel>
//입력값으로 레이아웃 경로를 입력
    (@LayoutRes private val layoutResId: Int) : AppCompatActivity() {
    protected lateinit var binding: T

    //추상
    abstract val viewModel: V
    abstract val TAG: String // 액티비티 태그


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        //엑티비티마다 다른 레이아웃 Id를 입력값으로 받음
        binding = DataBindingUtil.setContentView(this, layoutResId)
    }


    override fun onRestart() {
        super.onRestart()
//        Log.i(TAG, "onRestart")
    }


    override fun onStart() {
        super.onStart()
//        Log.i(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
//        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
//        Log.i(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
//        Log.i(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
//        Log.i(TAG, "onDestroy")
    }

//    abstract fun init()

    //엑티비티 이동
    inline fun <reified T : Activity> Context.startActivity() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

    protected fun showToast(msg: String) =
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
}