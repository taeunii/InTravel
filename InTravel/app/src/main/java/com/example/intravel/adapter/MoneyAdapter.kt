package com.example.intravel.adapter

import android.app.PendingIntent.getActivity
import android.content.ClipData.Item
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.DecimalFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.DetailMainActivity
import com.example.intravel.Fragment.TodoListFragment
import com.example.intravel.MainActivity
import com.example.intravel.client.Client
import com.example.intravel.client.SubClient
import com.example.intravel.data.MoneyData
import com.example.intravel.data.PayData
import com.example.intravel.databinding.CustomMoneyBinding
import com.example.intravel.databinding.ItemMoneyBinding
import com.example.intravel.databinding.ItemPayBinding
import retrofit2.Call
import retrofit2.Response
import java.lang.Long.parseLong


class MoneyAdapter(var context: Context, var moneyList:MutableList<MoneyData>):RecyclerView.Adapter<MoneyAdapter.Holder>() {
    // 페이 데이터 , 어댑터 생성


    fun insertMoney(money: MoneyData){
        moneyList.add(money)
        notifyDataSetChanged()
    }

    fun updateMoney(money: MoneyData, position:Int){
        moneyList[position] = money
        notifyDataSetChanged()
    }

    fun removeMoney(position: Int){
        moneyList.removeAt(position)
        notifyDataSetChanged()
    }

//    var payList = mutableListOf<PayData>(
//        PayData(0,9,"아이스크림",0,3000),
//        PayData(0,10,"몽쉘",0,5000),
//        PayData(0,11,"몽쉘",0,5000),
//        )
    var payList = mutableListOf<PayData>()
//    var payAdapter = PayAdapter(context,payList)

    inner class Holder(val binding: ItemMoneyBinding):RecyclerView.ViewHolder(binding.root) {
        // pay 작업
        init{

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyAdapter.Holder {
        return Holder(ItemMoneyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    interface OnItemClickListener{
        fun onItemClick(money: MoneyData, position: Int)
    }
    var onItemClickListener:OnItemClickListener?=null



    override fun onBindViewHolder(holder: MoneyAdapter.Holder, position: Int) {
        var money = moneyList.get(position)
        holder.binding.moneyTitle.text = money.moneyTitle
        holder.binding.mDefaultMoney.text = "예산 : ${DecimalFormat("#,###").format(money.expenses)}원"
        holder.binding.mMinus.text = "지출 : 90.000원"


        holder.binding.btnEdt.setOnClickListener{
            var edtDialog = CustomMoneyBinding.inflate(LayoutInflater.from(it.context))
            AlertDialog.Builder(it.context).run{
                setTitle("예산 카테고리 수정하기")
                setView(edtDialog.root)
                edtDialog.txt1.text = "수정할 이름을 입력해주세요."
                edtDialog.edtTitle.setText(money.moneyTitle)
                edtDialog.txt2.text = "수정할 금액을 입력해주세요."
                edtDialog.edtMoney.setText(money.expenses.toString())

                setPositiveButton("확인",object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        var m = MoneyData(money.moneyId,
                            money.travId,
                            edtDialog.edtTitle.text.toString(),
                            parseLong(edtDialog.edtMoney.text.toString()), // long형으로 변환
                        )
                        SubClient.retrofit.updateMoney(money.travId,m).enqueue(object:retrofit2.Callback<MoneyData>{
                            override fun onResponse(call: Call<MoneyData>, response: Response<MoneyData>) {
                                updateMoney(m,position)
                            }

                            override fun onFailure(call: Call<MoneyData>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        }) // enqueue
                    }
                }) // positive

                // positive 버튼 한개 더 만들어서 삭제로 하면 위치가 똑같고 얘 쓰면 왼쪽 끝에 나옴
                setNeutralButton("삭제",object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
//                        removeData(holder.adapterPosition)

                        // db 연결버전
                        SubClient.retrofit.deleteByIdMoney(money.moneyId).enqueue(object:retrofit2.Callback<Void>{
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                removeMoney(position)
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })//enqueu
                    }//onclick
                })//Neutral 삭제

                setNegativeButton("취소",null)
                show()
            } // dialog
        } // btnEdt

        var payAdapter = PayAdapter(context,payList) // 뷰바인더마다 어댑터 새로 생성해서 붙어야함 ;

            // 머니id 마다 불러온 데이터가 새로 들어올때마다 초기화되버려서 마지막 페이 데이터만 마지막 머니항목에 보이는거임
        // 어댑터 생성 코드들을 데이터연결 성공 코드에 붙인다면? >> 마지막 페이 데이터가 전부한테 붙음
        SubClient.retrofit.findAllPayList(money.moneyId).enqueue(object :retrofit2.Callback<List<PayData>>{
            override fun onResponse(call: Call<List<PayData>>, response: Response<List<PayData>>) {
                // 디비에 넣고 받은 데이터를 바로 리스트에 넣어서 초기화하는게 아니라 추가를 해야함?
                payAdapter.payList = response.body() as MutableList<PayData> // 머니 항목으로 페이 리스트 불러와서 저장
                payAdapter.notifyDataSetChanged()

                Log.d("response.body()","${response.body()}")
                holder.binding.payRecyclerView.adapter = payAdapter
                holder.binding.payRecyclerView.layoutManager = LinearLayoutManager(context)
            }
            override fun onFailure(call: Call<List<PayData>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })//findAll

        // 밖에 있으면 마지막 페이 데이터가 마지막 머니 항목에 붙고
//        holder.binding.payRecyclerView.adapter = payAdapter
//        holder.binding.payRecyclerView.layoutManager = LinearLayoutManager(context)


        holder.itemView.setOnClickListener {
//            onItemClickListener!!.onItemClick(money,position)
            if(holder.binding.payRecyclerView.isVisible == true){
                holder.binding.payRecyclerView.isVisible = false
            }
            else{
                holder.binding.payRecyclerView.isVisible = true
            }
        }



    } // onBindViewHolder


    override fun getItemCount(): Int {
        return moneyList.size
    }
}