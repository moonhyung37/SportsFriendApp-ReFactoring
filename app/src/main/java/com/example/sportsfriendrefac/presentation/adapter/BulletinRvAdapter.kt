package com.example.sportsfriendrefac.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseDiffCallback
import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.databinding.RvItemBulletinBinding
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.util.Constants
import org.json.JSONException
import org.json.JSONObject

class BulletinRvAdapter : ListAdapter<BulletinEntity, RecyclerView.ViewHolder>(BaseDiffCallback()) {
    //아이템뷰 리스트
    var List_user = emptyList<BulletinEntity>()

    init {
        setHasStableIds(true)
    }

    //엑티비티 Context
    var activityContext: Context? = null

    //리사이클러뷰의 아이템뷰의 클릭을 감지
    private var listener: OnItemClickListener? = null


    //아이템 클릭 인터페이스
    interface OnItemClickListener {
        fun onItemClick(v: View, data: BulletinEntity, pos: Int)
    }

    //아이템 클릭 인터페이스를 전달
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setContext(context: Context) {
        this.activityContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            RvItemBulletinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BulletinRvAdapter.ViewHolder).bind(List_user[position])
    }

    fun setData(List_user: List<BulletinEntity>) {
        this.List_user = List_user
        submitList(List_user)
    }

    //반환값으로 아이템뷰를 반환한다.
    inner class ViewHolder(private val binding: RvItemBulletinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BulletinEntity) {
            item.bltn_exer = "관심운동: " + item.bltn_exer
            item.bltn_addr = "동네: " + item.bltn_addr

            //모집 글 이미지
            val mainImgUrl = parseJsonMainImage(item.bltn_Main_img)
            if (mainImgUrl.isNotBlank()) {

                //모집 글 이미지가 있는 경우
                activityContext?.let {
                    Glide.with(it)
                        .load(Constants.Base_img_url + mainImgUrl)
                        .override(500, 500)
                        .into(binding.ivMainImgUrlItemBltn)
                }
            } else {
                //모집 글 이미지가 없는 경우(기본이미지)
                Glide.with(itemView).load(Constants.bltn_default_image).override(500, 500)
                    .into(binding.ivMainImgUrlItemBltn)
            }


            //DataBinding 사용하기위해 Item 객체 바인딩
            binding.bulletin = item
            //리사이클러뷰 아이템뷰 리스너
            itemView.setOnClickListener {
                //클릭한 아이템뷰의 인덱스 번호 변수에 저장
                val pos = adapterPosition
                //클릭이 되서 아이템뷰의 인덱스 번호를 받은 경우에만 실행
                if (pos != RecyclerView.NO_POSITION) {
                    //1.리사이클러뷰 수정, 삭제 팝업뷰 옵션버튼
                    listener?.onItemClick(itemView, item, pos)
                }
            }
        }
    }

    //Json 이미지 파일중에 썸내일 이미지만 추출하는 메서드
    //모집글의 이미지(썸네일 이미지)
    private fun parseJsonMainImage(loadData_json: String): String {
        //썸네일 이미지
        var bltn_Main_img_url = ""
        //이미지 URL이 있는 경우에만 실행
        if (loadData_json.isNotEmpty() && loadData_json != "null") {
            try {
                val jsonObject = JSONObject(loadData_json)
                //php에서 저장할 때 jsonArray의 키
                val jsonArray = jsonObject.getJSONArray("json_imgUri")
                //썸네일 이미지로 사용할 이미지 (0번)
                val item = jsonArray.getJSONObject(0)
                //모집 글 썸네일 이미지 추출
                bltn_Main_img_url = item.getString("image0")
                //ViewPager에 모집 글 이미지 추가
            } catch (e: JSONException) {
                Log.d("ParseError", "error : ", e)
            }
        }
        return bltn_Main_img_url
    }
}


