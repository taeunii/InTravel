package com.example.intravel.adapter

import android.app.PendingIntent.getActivity
import android.content.ClipData.Item
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.DetailMainActivity
import com.example.intravel.fragments.TodoListFragment
import com.example.intravel.MainActivity
import com.example.intravel.R
import com.example.intravel.client.Client
import com.example.intravel.client.SubClient
import com.example.intravel.data.MoneyData
import com.example.intravel.data.PayData
import com.example.intravel.databinding.CustomMoneyBinding
import com.example.intravel.databinding.CustomPayBinding
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

    var payList = mutableListOf<PayData>()


    inner class Holder(val binding: ItemMoneyBinding):RecyclerView.ViewHolder(binding.root) {

        init{

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyAdapter.Holder {
        return Holder(ItemMoneyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    interface OnItemChangeListener{
        fun onItemChange()
    }
    var onItemChangeListener:OnItemChangeListener?=null


    override fun onBindViewHolder(holder: MoneyAdapter.Holder, position: Int) {
        var money = moneyList.get(position)
        holder.binding.moneyTitle.text = money.moneyTitle
        holder.binding.mDefaultMoney.text = "예산 : ${DecimalFormat("#,###").format(money.expenses)}원"
        holder.binding.mMinus.text = "지출 : 90.000원"



        var payAdapter = PayAdapter(context,payList) // 뷰바인더마다 어댑터 새로 생성해서 붙어야함 ;

        // 머니 항목에 지출, 입금 계산 내역 붙이기
        var sumMinus = 0
        var sumPlus = 0
        var expenses = 0

            // 머니id 마다 불러온 데이터가 새로 들어올때마다 초기화되버려서 마지막 페이 데이터만 마지막 머니항목에 보이는거임
        // 어댑터 생성 코드들을 데이터연결 성공 코드에 붙인다면? >> 마지막 페이 데이터가 전부한테 붙음
        SubClient.retrofit.findAllPayList(money.moneyId).enqueue(object :retrofit2.Callback<List<PayData>>{
            override fun onResponse(call: Call<List<PayData>>, response: Response<List<PayData>>) {
                // 디비에 넣고 받은 데이터를 바로 리스트에 넣어서 초기화하는게 아니라 추가를 해야함?
                payAdapter.payList = response.body() as MutableList<PayData> // 머니 항목으로 페이 리스트 불러와서 저장
                payAdapter.notifyDataSetChanged()

                // 페이어댑터 함수 불러와서 계산
                sumPlus = payAdapter.sumPlus(response.body()as MutableList<PayData>)
                sumMinus = payAdapter.sumMinus(response.body()as MutableList<PayData>)
                expenses = (money.expenses+sumMinus+sumPlus).toInt()


                // 계산된 예산 붙이기
                holder.binding.mDefaultMoney.text = "예산 : ${DecimalFormat("#,###").format(expenses)}원"

                // 입금/지출 합계 화면에 붙이기
                holder.binding.mMinus.text = "지출: ${DecimalFormat("#,###").format(sumMinus)}원"
                holder.binding.mMinus.setTextColor(Color.parseColor("#DB0000"))

                holder.binding.mPlus.text = "입금: ${DecimalFormat("#,###").format(sumPlus)}원"
                holder.binding.mPlus.setTextColor(Color.parseColor("#2196F3"))

                Log.d("response.body()","${response.body()}")
                holder.binding.payRecyclerView.adapter = payAdapter
                holder.binding.payRecyclerView.layoutManager = LinearLayoutManager(context)
            }
            override fun onFailure(call: Call<List<PayData>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })//findAll


        // 머니 항목 클릭하면 페이 목록 온/오프
        holder.itemView.setOnClickListener {

            if(holder.binding.payRecyclerView.isVisible == true){
                holder.binding.payRecyclerView.isVisible = false
            }
            else{
                holder.binding.payRecyclerView.isVisible = true
            }

        }

        // 롱클릭 수정
        holder.itemView.setOnLongClickListener {
            var edtDialog = CustomMoneyBinding.inflate(LayoutInflater.from(it.context))
            AlertDialog.Builder(it.context).run {
                setTitle("예산 카테고리 수정하기")
                setView(edtDialog.root)
                edtDialog.txt1.text = "수정할 이름을 입력해주세요."
                edtDialog.edtTitle.setText(money.moneyTitle)
                edtDialog.txt2.text = "수정할 금액을 입력해주세요."
                edtDialog.edtMoney.setText(money.expenses.toString())

                setPositiveButton("확인", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        var m = MoneyData(
                            money.moneyId,
                            money.travId,
                            edtDialog.edtTitle.text.toString(),
                            parseLong(edtDialog.edtMoney.text.toString()), // long형으로 변환
                        )
                        SubClient.retrofit.updateMoney(money.moneyId, m)
                            .enqueue(object : retrofit2.Callback<MoneyData> {
                                override fun onResponse(
                                    call: Call<MoneyData>,
                                    response: Response<MoneyData>
                                ) {
                                    updateMoney(m, position)
                                    onItemChangeListener!!.onItemChange()
                                }

                                override fun onFailure(
                                    call: Call<MoneyData>,
                                    t: Throwable
                                ) {
                                    TODO("Not yet implemented")
                                }
                            }) // enqueue
                    }
                }) // positive

                // positive 버튼 한개 더 만들어서 삭제로 하면 위치가 똑같고 얘 쓰면 왼쪽 끝에 나옴
                setNeutralButton("삭제", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
//                        removeData(holder.adapterPosition)

                        // db 연결버전
                        SubClient.retrofit.deleteByIdMoney(money.moneyId)
                            .enqueue(object : retrofit2.Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    removeMoney(position)
                                    onItemChangeListener!!.onItemChange()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }
                            })//enqueu
                    }//onclick
                })//Neutral 삭제

                setNegativeButton("취소", null)
                show()
            } // dialog
            true
        }



        // pay 추가
        holder.binding.btnPayAdd.setOnClickListener {
            var payAddDialog = CustomPayBinding.inflate(LayoutInflater.from(it.context))
            AlertDialog.Builder(it.context).run{
                setTitle("수입/지출 항목 추가하기")
                setView(payAddDialog.root)

                setPositiveButton("확인",object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                        // 기본값 스트링 타입 0
                        var plus = "0"
                        var minus = "0"
                        // 라디오 체크에 따라 수입 지출 설정
                        if(payAddDialog.rdoIn.isChecked){
                            plus = payAddDialog.edtPayMoney.text.toString()
                        }else if(payAddDialog.rdoOut.isChecked){
                            minus = payAddDialog.edtPayMoney.text.toString()
                        }


                        var p = PayData(0,
                            money.travId,
                            money.moneyId,
                            payAddDialog.edtPayTitle.text.toString(),
                            parseLong(plus) ,
                            -parseLong(minus)  // long형으로 변환
                        )

                        SubClient.retrofit.insertPay(money.moneyId,p).enqueue(object:retrofit2.Callback<PayData>{
                            override fun onResponse(call: Call<PayData>, response: Response<PayData>) {
                                payAdapter.insertPay(p)
                                notifyDataSetChanged()
                                onItemChangeListener!!.onItemChange()

                            }

                            override fun onFailure(call: Call<PayData>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        }) // enqueue
                    }
                }) // positive

                setNegativeButton("취소",null)
                show()
            } // dialog
        } // btnPayAdd



        // 페이 수정 로직을 여기에 넣고
        // 페이 수정되면 머니화면도 업데이트 되도록
        payAdapter.onPayItemClickListener = object:PayAdapter.OnPayItemClickListener{
            override fun onPayItemClick(pay: PayData, position: Int) {

                var payEdtDialog = CustomPayBinding.inflate(LayoutInflater.from(context))
                AlertDialog.Builder(context).run{
                    setTitle("수입/지출 항목 수정하기")
                    setView(payEdtDialog.root)

                    // 기본값 스트링 타입 0
                    var plus = "0"
                    var minus = "0"

                    payEdtDialog.edtPayTitle.setText(pay.payTitle)

                    // 입금/ 지출 구분해서 데이터 수정해야함
                    // 수입/지출은 항목은 수정 못하게 함
                    // 입금 = 0 이면 지출
                    if(pay.plusAmt!!.toInt() == 0){
                        payEdtDialog.rdoOut.isChecked = true
                        payEdtDialog.rdoIn.isEnabled = false
                        payEdtDialog.edtPayMoney.setText((-pay.minusAmt!!).toString())
                    }
                    // 지출 = 0 이면 입금
                    else if(pay.minusAmt!!.toInt()==0){
                        payEdtDialog.rdoIn.isChecked = true
                        payEdtDialog.rdoOut.isEnabled = false
                        payEdtDialog.edtPayMoney.setText(pay.plusAmt.toString())
                    }


                    setPositiveButton("확인",object: DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            Log.d("parseLong(plus)",plus)
                            Log.d("parseLong(minus)",minus)

                            if(pay.plusAmt!!.toInt() == 0){
                                minus = payEdtDialog.edtPayMoney.text.toString()
                            }
                            // 지출 = 0 이면 입금
                            else if(pay.minusAmt!!.toInt()==0){
                                plus = payEdtDialog.edtPayMoney.text.toString()
                            }

                            var p = PayData(0,
                                pay.travId,
                                pay.moneyId,
                                payEdtDialog.edtPayTitle.text.toString(),
                                parseLong(plus) ,
                                -parseLong(minus)  // long형으로 변환
                            )

                            SubClient.retrofit.updatePay(pay.payId,p).enqueue(object:retrofit2.Callback<PayData>{
                                override fun onResponse(call: Call<PayData>, response: Response<PayData>) {
                                    payAdapter.updatePay(p,position)
                                    notifyDataSetChanged()
                                    onItemChangeListener!!.onItemChange()

                                }

                                override fun onFailure(call: Call<PayData>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }
                            }) // enqueue
                        }
                    }) // positive

                    setNeutralButton("삭제",object:DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            SubClient.retrofit.deleteByIdPay(pay.payId).enqueue(object:retrofit2.Callback<Void>{
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    payAdapter.removePay(position)
                                    notifyDataSetChanged()
                                    onItemChangeListener!!.onItemChange()
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

            } // 수정

        } //onPayItemClickListener

    } // onBindViewHolder


    override fun getItemCount(): Int {
        return moneyList.size
    }
}