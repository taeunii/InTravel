package com.example.intravel.adapter

import android.content.Context
import android.content.DialogInterface
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.R
import com.example.intravel.client.Client
import com.example.intravel.data.MainData
import com.example.intravel.databinding.CustomDdayBinding
import com.example.intravel.databinding.ItemMainBinding
import retrofit2.Call
import retrofit2.Response

class MainAdapter(var mainList: MutableList<MainData>):RecyclerView.Adapter<MainAdapter.MainHolder>() {

    // 디데이 계산은 어댑터에서 해야할 것 같은데.....
//    fun ddayCal(data: MainData, position: Int):String{
//
//        return "${data.startDate}"-"${data.createDate}"
//    }

    fun updateData(data:MainData,position: Int){
        mainList[position] = data
        notifyDataSetChanged()
    }

    fun removeData(position: Int){
        mainList.removeAt(position)
        notifyDataSetChanged()
    }
    
    class MainHolder(var binding: ItemMainBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainHolder {
        return MainHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MainAdapter.MainHolder, position: Int) {
        val data = mainList.get(position)
        holder.binding.itemTitle.text = data.tTitle
        holder.binding.itemCate.text = data.cate
        //holder.binding.itemTextview.text = "D"
        holder.binding.itemDday.text = data.dday.toString()

        // 수정
        // 다이얼로그 창 띄우기
//        val cateData = resources.getStringArray(R.array.cateList)
//        var cateSelected:String?=null

        holder.binding.btnEdt.setOnClickListener{
            val dialogEdit = CustomDdayBinding.inflate(LayoutInflater.from(it.context))
//            val cateAdapter = ArrayAdapter(it.context,android.R.layout.simple_list_item_1,cateData)
//            dialogEdit.cateSpinner.adapter = cateAdapter

            AlertDialog.Builder(it.context).run{
                setTitle("디데이 수정하기")
                setMessage("날짜를 선택해주세요")
                setView(dialogEdit.root)

                dialogEdit.ddayName.setText(data.tTitle.toString())
                dialogEdit.edtStart.setText(data.startDate.toString())
                dialogEdit.edtEnd.setText(data.endDate.toString())

                dialogEdit.calendarView.setOnDateChangeListener{calendarView, year, month, date ->
                    dialogEdit.calendarView.setDate(System.currentTimeMillis())
                }

                dialogEdit.edtStart.setOnClickListener {
                    dialogEdit.calendarView.setOnDateChangeListener { calendarView, year, month, date ->
                        dialogEdit.edtStart.setText("${year}.${month+1}.${date}")
                    }
                }

                // 마감 날짜를 눌렀을 때 캘린더 활성화
                dialogEdit.edtEnd.setOnClickListener {
                    dialogEdit.calendarView.setOnDateChangeListener { calendarView, year, month, date ->
                        dialogEdit.edtEnd.setText("${year}.${month+1}.${date}")
                    }
                }
                
                setPositiveButton("확인",object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val d = MainData( // 수정된 데이터 생성
                            mainList.get(position).tId,
                            dialogEdit.ddayName.text.toString(),
                            mainList[position].createDate,
                            dialogEdit.edtStart.text.toString(),
                            dialogEdit.edtEnd.text.toString(),
                            "cate",
                            -2,
                            'Y'
                        )
                        updateData(d,holder.adapterPosition)

//                        // db 연결 버전
//                        Client.retrofit.update(d.tId,d).enqueue(object:retrofit2.Callback<MainData>{
//                            override fun onResponse(call: Call<MainData>,response: Response<MainData>) {
//                                response.body()?.let { it1 -> updateData(it1,holder.adapterPosition) }
//                            }
//
//                            override fun onFailure(call: Call<MainData>, t: Throwable) {
//                                TODO("Not yet implemented")
//                            }
//                        }) // enqueu
//                    }
                    } // onclick
                }) // positive 확인

                // positive 버튼 한개 더 만들어서 삭제로 하면..
                setNeutralButton("삭제",object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        removeData(holder.adapterPosition)

                        // db 연결버전
//                        Client.retrofit.deleteById(data.tId).enqueue(object:retrofit2.Callback<Void>{
//                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                                removeData(holder.adapterPosition)
//                            }
//
//                            override fun onFailure(call: Call<Void>, t: Throwable) {
//                                TODO("Not yet implemented")
//                            }
//
//                        })//enqueu
                    }//onclick

                })//positive 삭제

                setNegativeButton("취소",null)
                show()
            }
        }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }
}