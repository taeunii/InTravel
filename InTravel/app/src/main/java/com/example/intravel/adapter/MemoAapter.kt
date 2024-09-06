package com.example.intravel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.data.Memo
import com.example.intravel.databinding.ItemMemolistBinding

class MemoAapter(var memoList: MutableList<Memo>):RecyclerView.Adapter<MemoAapter.MemoHolder>() {

    class MemoHolder(val binding: ItemMemolistBinding):RecyclerView.ViewHolder(binding.root)

    // DB 연결용
    interface OnItemClickListener {
        fun onItemClick(memo: Memo, pos: Int)
    }
    var onItemClickListener:OnItemClickListener?= null

    // 추가
    fun addMemo(memo: Memo) {
        memoList.add(memo)
        notifyDataSetChanged()
    }

    // 수정
    fun updateMemo(memo: Memo, position: Int) {
        memoList[position] = memo
        notifyDataSetChanged()
    }

    // 삭제
    fun removeMemo(position: Int) {
        memoList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoAapter.MemoHolder {
        return MemoHolder(ItemMemolistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MemoAapter.MemoHolder, position: Int) {
        val memoItem = memoList[position]

        // 초기 데이터 설정
        holder.binding.mTitle.text = memoItem.mTitle
        holder.binding.choiceDate.text = memoItem.choiceDate

        // 수정, 삭제
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(memoItem, position)
        }
    }

    override fun getItemCount(): Int {
        return memoList.size
    }
}