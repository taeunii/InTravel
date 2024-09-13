package com.example.intravel.adapter

import android.content.DialogInterface
import android.graphics.Color
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

class MemoAapter(var memoList: MutableList<Memo>, val tStartDate: String?, val tEndDate: String?, val tComplete:Char?):RecyclerView.Adapter<MemoAapter.MemoHolder>() {

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

        // 상세보기 및 수정 (다이얼로그 창)
        // tComplete 들고와서 Y이면 읽을수만 있게
        if(tComplete == 'Y'){
            holder.itemView.setOnClickListener {
                val updateDialog = CustomMemowriteBinding.inflate(LayoutInflater.from(it.context))

                updateDialog.MemoDate.isEnabled = false
                updateDialog.edtTitle.isEnabled = false
                updateDialog.edtContent.isEnabled = false

                updateDialog.MemoDate.setTextColor(Color.parseColor("#111111"))
                updateDialog.edtTitle.setTextColor(Color.parseColor("#111111"))
                updateDialog.edtContent.setTextColor(Color.parseColor("#111111"))

                updateDialog.MemoDate.setText(memoItem.choiceDate)
                updateDialog.edtTitle.setText(memoItem.memoTitle)
                updateDialog.edtContent.setText(memoItem.memoContent)

                AlertDialog.Builder(it.context).run {
                    setTitle("여행 메모")
                    setView(updateDialog.root)

                    setPositiveButton("확인",null)

                    setNeutralButton("삭제", object :DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            SubClient.retrofit.deleteByIdMemo(memoItem.memoId).enqueue(object :retrofit2.Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    removeMemo(holder.adapterPosition)
                                }
                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                }
                            })
                        }
                    })  //setNeutralButton

                    show()
                }
            } // onClickListener Y

        }else{
            holder.itemView.setOnClickListener {
                val updateDialog = CustomMemowriteBinding.inflate(LayoutInflater.from(it.context))
                val memoWriteDialog = CustomMemodateBinding.inflate(LayoutInflater.from(it.context))

                updateDialog.MemoDate.setText(memoItem.choiceDate)
                updateDialog.edtTitle.setText(memoItem.memoTitle)
                updateDialog.edtContent.setText(memoItem.memoContent)

                AlertDialog.Builder(it.context).run {
                    setTitle("여행 메모")
                    setView(updateDialog.root)

                    // 캘린더 클릭시 다이얼로그
                    updateDialog.btnMemoDate.setOnClickListener {
                        AlertDialog.Builder(it.context).run {
                            setTitle("여행 날짜 선택")
                            setView(memoWriteDialog.root)

                            var selectedDate: String = updateDialog.MemoDate.text.toString()
                            memoWriteDialog.choiceDate.setText(selectedDate)

                            // tStartDate와 tEndDate를 Date 객체로 변환
                            val startDate: Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(tStartDate) ?: Date()
                            val endDate: Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(tEndDate) ?: Date()

                            // 캘린더에서 선택 가능한 날짜 범위 설정
                            memoWriteDialog.memoCalendarView.minDate = startDate.time
                            memoWriteDialog.memoCalendarView.maxDate = endDate.time

                            if (selectedDate.isNotEmpty()) {
                                val dateParts = selectedDate.split(".")
                                if (dateParts.size == 3) {
                                    val year = dateParts[0].toInt()
                                    val month = dateParts[1].toInt() - 1
                                    val day = dateParts[2].toInt()

                                    val calendar = Calendar.getInstance()
                                    calendar.set(year, month, day)
                                    memoWriteDialog.memoCalendarView.date = calendar.timeInMillis
                                }
                            }

                            // 캘린더 날짜 선택 시 하부에 선택된 날짜 표현
                            memoWriteDialog.memoCalendarView.setOnDateChangeListener { calendarView, year, month, date  ->
                                var m_month = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
                                var m_date = if (date < 10) "0${date}" else "$date"
                                selectedDate = "$year.$m_month.$m_date"  // YYYY.MM.DD 형식
                                memoWriteDialog.choiceDate.setText(selectedDate)
                            }

                            setPositiveButton("확인", object :DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    if (selectedDate.isEmpty()) {
                                        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                                        selectedDate = dateFormat.format(Date())
                                    }
                                    updateDialog.MemoDate.setText(selectedDate)
                                }
                            })
                            setNegativeButton("취소", null)
                            show()
                        }
                    }   //btnMemoDate

                    setPositiveButton("확인", object :DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            val memoItem = Memo(memoList.get(position).memoId,
                                memoList.get(position).travId,
                                updateDialog.edtTitle.text.toString(),
                                updateDialog.edtContent.text.toString(),
                                updateDialog.MemoDate.text.toString(),
                                "")
                            SubClient.retrofit.updateMemo(memoItem.memoId, memoItem).enqueue(object :retrofit2.Callback<Memo> {
                                override fun onResponse(call: Call<Memo>, response: Response<Memo>) {
//                                Log.d("memo update","${response.body()}")
                                    response.body()?.let { item -> updateMemo(item, holder.adapterPosition) }
                                }
                                override fun onFailure(call: Call<Memo>, t: Throwable) {
                                }
                            })
                        }
                    })  //setPositiveButton

                    setNeutralButton("삭제", object :DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            SubClient.retrofit.deleteByIdMemo(memoItem.memoId).enqueue(object :retrofit2.Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    removeMemo(holder.adapterPosition)
                                }
                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                }
                            })
                        }
                    })  //setNeutralButton

                    setNegativeButton("취소", null)
                    show()
                }
            } // onClickListener N
        }

    }

    override fun getItemCount(): Int {
        return memoList.size
    }


}