package com.example.sportsfriendrefac.presentation.bulletin

import android.app.Activity
import android.content.Intent
import android.view.View

import androidx.databinding.DataBindingUtil


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseActivity
import com.example.sportsfriendrefac.databinding.ActivityBulletinInforBinding
import com.example.sportsfriendrefac.databinding.ActivityMainBinding
import com.example.sportsfriendrefac.presentation.MainActivity
import com.example.sportsfriendrefac.presentation.viewModel.BulletinInforViewModel
import com.example.sportsfriendrefac.presentation.viewModel.MainViewModel
import timber.log.Timber

/* 모집 글 상세정보 엑티비티  */
class BulletinInforActivity : BaseActivity<ActivityBulletinInforBinding, BulletinInforViewModel>
    (R.layout.activity_bulletin_infor),
    View.OnClickListener {
    override val viewModel: BulletinInforViewModel by viewModels()
    override val TAG: String
        get() = "BulletinInforActivity"
    var bltn_idx: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnInsertCommentBltnInfor.setOnClickListener(this)
        binding.ivRefreshBltnInfor.setOnClickListener(this)
        binding.ivProfileImgBltnInfor.setOnClickListener(this)


        //툴바 사용 설정
        setSupportActionBar(binding.toolBarBulletinInfor)
        //액션바 타이틀 없애기
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true

        //intent변수는 따로 선언안 해주어도 사용가능
        if (intent.hasExtra("1")) {
            bltn_idx = intent.getStringExtra("1")
        }

    }


    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 댓글 추가
            binding.btnInsertCommentBltnInfor.id -> {

            }
            //2) 댓글 새로고침
            binding.ivRefreshBltnInfor.id -> {

            }
            //3) 프로필 사진 클릭 (프로필 정보 확인)
            binding.ivProfileImgBltnInfor.id -> {

            }
            else -> {
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        //이미지의 기본 id를 안드로이드에서 기본적으로 정해놓음.
        android.R.id.home -> {
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                //엑티비티에서 갖고올 데이터
//                putExtra("키, " 값 ")
            }

            setResult(RESULT_OK, intent)
            if (!isFinishing) finish()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


}




