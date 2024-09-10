package com.example.intravel.adapter

import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.client.SubClient
import com.example.intravel.data.Memo
import com.example.intravel.databinding.CustomMemodateBinding
import com.example.intravel.databinding.CustomMemowriteBinding
import com.example.intravel.databinding.ItemMemolistBinding
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MemoAapter(var memoList: MutableList<Memo>):RecyclerView.Adapter<MemoAapter.MemoHolder>() {

    class MemoHolder(val binding: ItemMemolistBinding):RecyclerView.ViewHolder(binding.root)

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
        holder.binding.mTitle.text = memoItem.memoTitle
        holder.binding.choiceDate.text = memoItem.choiceDate

        // 수정
        holder.itemView.setOnClickListener {

        }

//        // 삭제
//        holder.itemView.setOnClickListener {
//            AlertDialog.Builder(it.context).run {
//                setTitle("삭제하시겠습니까?")
//                setPositiveButton("삭제", object :DialogInterface.OnClickListener {
//                    override fun onClick(p0: DialogInterface?, p1: Int) {
//                        SubClient.retrofit.deleteByIdMemo(memoItem.memoId).enqueue(object :retrofit2.Callback<Void> {
//                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                                removeMemo(holder.adapterPosition)
//                            }
//                            override fun onFailure(call: Call<Void>, t: Throwable) {
//                            }
//                        })
//                    }
//                })
//                setNegativeButton("닫기", null)
//                show()
//            }
//        }
    }

    override fun getItemCount(): Int {
        return memoList.size
    }
}