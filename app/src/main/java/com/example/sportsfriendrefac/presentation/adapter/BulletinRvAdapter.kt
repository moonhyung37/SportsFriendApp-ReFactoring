package com.example.sportsfriendref

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.base.BaseDiffCallback
import com.example.sportsfriendrefac.databinding.RvItemBulletinBinding
import com.example.sportsfriendrefac.databinding.RvItemMyBulletinBinding
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.util.Constants
import org.json.JSONException
import org.json.JSONObject

class BulletinRvAdapter : ListAdapter<BulletinEntity, RecyclerView.ViewHolder>(BaseDiffCallback()) {
    /*   init {
           setHasStableIds(true)
       }*/

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


    // 뷰의 타입을 정해주는 곳이다.
    override fun getItemViewType(position: Int): Int {
        return when (currentList[position].bltn_flag) {
            1 -> 1  // 전체 모집글 아이템 뷰
            else -> 2    //뷰타입 2번: 내가 작성한 모집 글 아이템 뷰
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            //뷰타입 1번: 전체 모집글 아이템 뷰
            1 -> {
                val binding = RvItemBulletinBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
                BulletinViewHolder(binding)
            }
            //뷰타입 2번: 내가 작성한 모집 글 아이템 뷰
            else -> {
                val binding = RvItemMyBulletinBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
                MyBulletinViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (currentList[position].bltn_flag) {

            1 -> {
                (holder as BulletinRvAdapter.BulletinViewHolder).bind(getItem(position))

            }

            else -> {
                (holder as BulletinRvAdapter.MyBulletinViewHolder).bind(getItem(position))
            }
        }

    }

    //아이템 추가
    //바텀네비게이션 프래그먼트 안에서 실행했기 때문에 별도로 flag에 숫자를 입력하지 않았음.
    fun addItem(item: BulletinEntity) {
        val newList = currentList.toMutableList()
        newList.add(0, item)
        submitList(newList)


    }

    //아이템 삭제
    fun deleteItem(pos: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(pos)
        submitList(newList)
        App.instance.flagBulletinSelect = 1
    }

    //아이템 수정
    fun editItem(pos: Int, item: BulletinEntity) {
        val newList = currentList.toMutableList()
        newList[pos] = item
        submitList(newList)
        App.instance.flagBulletinSelect = 1
    }

    //아이템 리스트 초기화
    fun clear() {
        currentList.toMutableList().clear()
    }

    //1.전체 모집 글 아이템뷰
    inner class BulletinViewHolder(private val binding: RvItemBulletinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BulletinEntity) {   //DataBinding 사용하기위해 Item 객체 바인딩
            var mainImgUrl = ""
            //-모집 글 이미지가 있는 경우에만
            //-""""가 들어가있는 경우를 제외하기 위해서 length=2 사용
            if (item.bltn_img_url != null && item.bltn_img_url.length != 2) {
                mainImgUrl = parseJsonMainImage(item.bltn_img_url)
            }


            //모집 글 이미지가 있는 경우에만
            if (mainImgUrl.isNotBlank()) {
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

    //2.내가 작성한 모집 글 아이템 뷰
    inner class MyBulletinViewHolder(private val binding: RvItemMyBulletinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BulletinEntity) {
            var mainImgUrl = ""
            //-모집 글 이미지가 있는 경우에만
            //-""""가 들어가있는 경우를 제외하기 위해서 length=2 사용
            if (item.bltn_img_url != null && item.bltn_img_url.length != 2) {
                mainImgUrl = parseJsonMainImage(item.bltn_img_url)
            }
            //모집 글 이미지가 있는 경우에만
            if (mainImgUrl.isNotBlank()) {
                activityContext?.let {
                    Glide.with(it)
                        .load(Constants.Base_img_url + mainImgUrl)
                        .override(500, 500)
                        .into(binding.ivMainImgUrlItemMyBltn)
                }
            } else {
                //모집 글 이미지가 없는 경우(기본이미지)
                Glide.with(itemView).load(Constants.bltn_default_image).override(500, 500)
                    .into(binding.ivMainImgUrlItemMyBltn)
            }

            binding.bulletin = item
            binding.ivOptionBulletinItem.setOnClickListener {
                val pos = adapterPosition
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
                //서버에서 받아온 Json 형식이 아닌 경우
                return loadData_json
            }
        }
        return bltn_Main_img_url
    }


}


