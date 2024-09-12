package com.example.intravel.adapter

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.intravel.client.SubClient
import com.example.intravel.data.MoneyData
import com.example.intravel.data.PayData
import com.example.intravel.databinding.CustomPayBinding
import com.example.intravel.databinding.ItemPayBinding
import retrofit2.Call
import retrofit2.Response
import java.lang.Long.parseLong

class PayAdapter(var context:Context,var payList: MutableList<PayData>):RecyclerView.Adapter<PayAdapter.Holder>() {

    fun insertPay(pay: PayData){
        payList.add(pay)
        notifyDataSetChanged()
    }

    fun updatePay(pay: PayData, position:Int){
        payList[position] = pay
        notifyDataSetChanged()
    }

    fun removePay(position: Int){
        payList.removeAt(position)
        notifyDataSetChanged()
    }

    // 해당 메모의 페이리스트만 넘어오니까...............
    fun sumMinus(payList: MutableList<PayData>):Int{

        var minusResult = 0

        // 페이리스트 안에서 한개씩 꺼내는데
        for(i in payList){
            if(i.plusAmt!!.toInt() == 0){
                // 지출 합계
                Log.d("i.minusAmt","${i.minusAmt}")
                minusResult += i.minusAmt!!.toInt()
            }
        }
        // 입금 = 0 이면 지출

        return minusResult
    }

    fun sumPlus(payList: MutableList<PayData>):Int{
        var plusResult = 0

        for(i in payList){
            if(i.minusAmt!!.toInt()==0){
                // 입금 합계
                Log.d("i.plusAmt","${i.plusAmt}")
                plusResult += i.plusAmt!!.toInt()
            }
        }
        return plusResult
    }



    inner class Holder(var binding: ItemPayBinding):RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayAdapter.Holder {
        return Holder(ItemPayBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    interface OnPayItemClickListener{
        fun onPayItemClick(pay: PayData, position: Int)
    }
    var onPayItemClickListener:OnPayItemClickListener?=null


    override fun onBindViewHolder(holder: PayAdapter.Holder, position: Int) {
        var pay = payList.get(position)
        Log.d("pay Item", "${payList[position]}")
        Log.d("pay position", "${position}")
        holder.binding.payTitle.text = pay.payTitle

        // 입금(파랑)/ 지출(빨강) 색 구분해서 하나만 출력
        // 입금 = 0 이면 지출
        if(pay.plusAmt!!.toInt() == 0){
            holder.binding.pMoney.setTextColor(Color.parseColor("#DB0000"))
            holder.binding.pMoney.text = "${DecimalFormat("#,###").format(pay.minusAmt)}원"
        }
        // 지출 = 0 이면 입금
        else if(pay.minusAmt!!.toInt()==0){
            holder.binding.pMoney.setTextColor(Color.parseColor("#2196F3"))
            holder.binding.pMoney.text = "${DecimalFormat("#,###").format(pay.plusAmt)}원"
        }

        // 수정
        holder.itemView.setOnClickListener{
            // 페이가 수정되면 머니화면도 업데이트되어야 함
            // 온클릭 인터페이스 생성하고 머니 어댑터에서 처리하면 될지도?
            onPayItemClickListener!!.onPayItemClick(pay,position)

        } // 수정

    } // onBindViewHolder

    override fun getItemCount(): Int {
        return payList.size
    }
}