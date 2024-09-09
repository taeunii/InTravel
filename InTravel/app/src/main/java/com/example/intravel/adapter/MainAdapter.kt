package com.example.intravel.adapter

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.client.Client
import com.example.intravel.data.TravelData
import com.example.intravel.databinding.CustomDdayBinding
import com.example.intravel.databinding.ItemMainBinding
import retrofit2.Call
import retrofit2.Response
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.Date

class MainAdapter(var mainList: MutableList<TravelData>):RecyclerView.Adapter<MainAdapter.MainHolder>() {

    // 클래스 멤버 변수로 선언
    // dday 계산 및 완료 여부 확인할 때 필요함
    private var sysDate = Date(System.currentTimeMillis())
    private var dateFormat = SimpleDateFormat("yyyyMMdd")
    private var today = parseInt(dateFormat.format(sysDate))

    // 오늘 날짜 활용해야함, 생성일X
    fun ddayCal(data:TravelData):String{
    // 계산하기 위한 int 변환
        var startDate = parseInt(data.startDate)

//        var endDate = parseInt(data.endDate)

        var result = startDate-today // 시작 날짜 - 오늘 날짜

        var dday = ""  // 반환할 string 값

        // 디데이 +/- 계산
        if(result <0){
        // 오늘 > 시작
            result = -result
            dday = "+${result}"

        }
        else if(result == 0){
            dday = "-day"
        }
        else{
        // 오늘 < 시작
            dday = "-${result}"
        }

        return dday
    }

//    fun updateComplete(data:TravelData, position: Int){
//        mainList[position] = data
//        notifyDataSetChanged()
//
//    }

    fun updateData(data:TravelData, position: Int){
        mainList[position] = data
        notifyDataSetChanged()
    }

    fun removeData(position: Int){
        mainList.removeAt(position)
        notifyDataSetChanged()
    }

    fun insertData(data:TravelData){
        mainList.add(data)
        notifyDataSetChanged()
    }
    
    class MainHolder(var binding: ItemMainBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainHolder {
        return MainHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    interface OnItemClickListener{
        fun onItemClick(data: TravelData,dday:String,position: Int)
    }
    var onItemClickListener:OnItemClickListener?=null

    override fun onBindViewHolder(holder: MainAdapter.MainHolder, position: Int) {
        val data = mainList.get(position)
        val dday = ddayCal(data)


        if(data.travComplete == 'Y'){
            holder.binding.frontLayout.isVisible = false
            holder.binding.completeLayout.isVisible = true
            //holder.binding.itemTextview.isVisible = false // 장소까지 숨김
            holder.binding.comTitle.text = data.travTitle
            holder.binding.comPeriod.text = "${data.startDate}~${data.endDate}"
            holder.binding.comCate.text = "같이 갔던 사람 : ${data.cate}"

        }else{
            holder.binding.frontLayout.isVisible = true
            holder.binding.completeLayout.isVisible = false

            holder.binding.itemTitle.text = data.travTitle
            holder.binding.itemCate.text = data.cate
            holder.binding.itemDday.text = dday
        }


        // 디데이 클릭하면 서브로 넘어가기
        // 정보만 넘기고 데이터 안 받아와도 됨 아마도??
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(data,dday,position)
        }


        // 수정
        // 다이얼로그 창 띄우기
        // 카테고리스피너
        val cateList = mutableListOf("---선택해주세요---","혼자","친구","가족","연인")
        var cateSelected = data.cate

        holder.binding.btnEdt.setOnClickListener{
            val dialogEdit = CustomDdayBinding.inflate(LayoutInflater.from(it.context))

            // 스피너 어댑터
            val cateAdapter = ArrayAdapter(it.context,android.R.layout.simple_list_item_1,cateList)
            dialogEdit.cateSpinner.adapter = cateAdapter

            AlertDialog.Builder(it.context).run{
                setTitle("디데이 수정하기")
                setMessage("날짜를 선택해주세요")
                setView(dialogEdit.root)

                dialogEdit.ddayName.setText(data.travTitle.toString())
                dialogEdit.edtStart.setText(data.startDate.toString())
                dialogEdit.edtEnd.setText(data.endDate.toString())

                dialogEdit.calendarView.setOnDateChangeListener{calendarView, year, month, date ->
                    dialogEdit.calendarView.setDate(System.currentTimeMillis())
                }

                dialogEdit.edtStart.setOnClickListener {
                    dialogEdit.calendarView.setOnDateChangeListener { calendarView, year, month, date ->
                        var e_month = ""
                        var e_date = ""

                        // 한자리 수는 0붙여서 나오게
                        if(month+1 < 10){
                            e_month = "0${month+1}"

                            if(date < 10){
                                e_date = "0${date}"
                            }
                            else{
                                e_date = date.toString()
                            }

                        }else if(month+1 > 10){
                            e_month = (month+1).toString()

                            if(date < 10){
                                e_date ="0${date}"
                            }
                            else{
                                e_date =date.toString()
                            }
                        }

                        dialogEdit.edtStart.setText("${year}${e_month}${e_date}")
                    }
                }

                // 마감 날짜를 눌렀을 때 캘린더 활성화
                dialogEdit.edtEnd.setOnClickListener {
                    dialogEdit.calendarView.setOnDateChangeListener { calendarView, year, month, date ->
                        var e_month = ""
                        var e_date = ""

                        // 한자리 수는 0붙여서 나오게
                        if(month+1 < 10){
                            e_month = "0${month+1}"

                            if(date < 10){
                                e_date = "0${date}"
                            }
                            else{
                                e_date = date.toString()
                            }

                        }else if(month+1 > 10){
                            e_month = (month+1).toString()

                            if(date < 10){
                                e_date ="0${date}"
                            }
                            else{
                                e_date =date.toString()
                            }
                        }

                        dialogEdit.edtEnd.setText("${year}${e_month}${e_date}")
                    }
                }

                dialogEdit.cateSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        cateSelected = cateList.get(position)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        cateSelected = data.cate
                    }
                }


                
                setPositiveButton("확인",object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val d = TravelData( // 수정된 데이터 생성
                            mainList.get(position).travId,
                            dialogEdit.ddayName.text.toString(),
                            mainList[position].createDate,
                            dialogEdit.edtStart.text.toString(),
                            dialogEdit.edtEnd.text.toString(),
                            cateSelected,
                            'N')
//                        updateData(d,holder.adapterPosition)

//                        // db 연결 버전
                        Client.retrofit.update(d.travId,d).enqueue(object:retrofit2.Callback<TravelData>{
                            override fun onResponse(call: Call<TravelData>, response: Response<TravelData>) {
                                response.body()?.let { it1 -> updateData(it1,holder.adapterPosition) }
                            }

                            override fun onFailure(call: Call<TravelData>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        }) // enqueu
                    } // onclick
                }) // positive 확인

                // positive 버튼 한개 더 만들어서 삭제로 하면..
                setNeutralButton("삭제",object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
//                        removeData(holder.adapterPosition)

                        // db 연결버전
                        Client.retrofit.deleteById(data.travId).enqueue(object:retrofit2.Callback<Void>{
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                removeData(holder.adapterPosition)
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                TODO("Not yet implemented")
                            }

                        })//enqueu
                    }//onclick

                })//positive 삭제
                setNegativeButton("취소",null)
                show()
            }//dialog
        }//btnEdt


        // findAll로 받아온 전체 리스트 활용
        // 오늘 날짜가 enddate 보다 크다면 디비에 tId, Y 전달, 업데이트랑 비슷하게 하면 될듯?
        // 마감 날짜 받아오기
        var endDate = parseInt(data.endDate)
        if(today == endDate+1){ // 완료 날짜 다음날이면 확인 창
            // 여행 완료 됐는지 묻는 창
            AlertDialog.Builder(holder.itemView.context).run{
                setTitle("여행 완료 여부")
                setMessage("${data.travTitle}이 완료된 여행이 맞나요?")
                setPositiveButton("예",object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                        val d = TravelData( // 수정된 데이터 생성
                            data.travId,
                            data.travTitle,
                            data.createDate,
                            data.startDate,
                            data.endDate,
                            cateSelected,
                            'Y')

                        Client.retrofit.update(d.travId, d).enqueue(object:retrofit2.Callback<TravelData>{
                            override fun onResponse(call: Call<TravelData>,response: Response<TravelData>) {
                                response.body()?.let { updateData(it, position) }

                            }

                            override fun onFailure(call: Call<TravelData>, t: Throwable) {
                                TODO("Not yet implemented")
                            }

                        })
                    }

                })
                setNegativeButton("아니오",null)
                show()

            }

            // 아니라면 수정해달라고 하기
        } //if

    }//onBindViewHolder

    override fun getItemCount(): Int {
        return mainList.size
    }
}