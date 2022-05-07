package com.example.sportsfriendrefac.base

import androidx.recyclerview.widget.DiffUtil
import com.example.sportsfriendrefac.domain.model.BaseData

//모든 아이템을 상속하는 BaseData를 사용해 아이템을 비교
internal open class BaseDiffCallback<T : BaseData> : DiffUtil.ItemCallback<T>() {
    //ItemId로 같은 객체인지 확인
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.idx == newItem.idx
    }

    //두 아이템이 같은 데이터를 갖고있는지 확인
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}