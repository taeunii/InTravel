package com.example.intravel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.data.Memo
import com.example.intravel.databinding.ItemMemolistBinding

class MemoAapter(val memoList: MutableList<Memo>):RecyclerView.Adapter<MemoAapter.Holder>() {
    class Holder(val binding: ItemMemolistBinding):RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClick(memo: Memo, pos: Int)
    }
    var onItemClickListener:OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoAapter.Holder {
        return Holder(ItemMemolistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MemoAapter.Holder, position: Int) {
        val memoItem = memoList[position]
        holder.binding.mTitle.text = memoItem.mTitle
        holder.binding.mDate.setText("여행 날짜")

        // 수정
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(memoItem, position)
        }
    }

    override fun getItemCount(): Int {
        return memoList.size
    }
}