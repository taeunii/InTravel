package com.example.intravel.Fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.DetailMainActivity
import com.example.intravel.MainActivity
import com.example.intravel.R
import com.example.intravel.adapter.MoneyAdapter
import com.example.intravel.adapter.PayAdapter
import com.example.intravel.client.SubClient
import com.example.intravel.data.MoneyData
import com.example.intravel.data.PayData
import com.example.intravel.data.TravelData
import com.example.intravel.databinding.ActivitySubmainBinding
import com.example.intravel.databinding.CustomMoneyBinding
import com.example.intravel.databinding.FragmentMoneytabBinding
import com.example.intravel.databinding.ItemMoneyBinding
import retrofit2.Call
import retrofit2.Response
import java.lang.Long.parseLong


class MoneyTabFragment : Fragment() {

    private lateinit var binding: FragmentMoneytabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMoneytabBinding.inflate(inflater, container, false)
        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding.moneyRecyclerView) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }


        // 데이터 생성
        var moneyList = mutableListOf<MoneyData>()
        // 어댑터 생성
        var moneyAdapter = MoneyAdapter(requireContext(), moneyList)
        // 리사이클러뷰 어댑터 연결
        binding.moneyRecyclerView.adapter = moneyAdapter
        // 레이아웃 설정
        binding.moneyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 메인에서 보낸 travId 받아오기
        val tId = activity?.intent?.getLongExtra("tId", 0) ?: 0

        // 첫화면에 리스트 불러오기
        SubClient.retrofit.findAllMoneyList(tId)
            .enqueue(object : retrofit2.Callback<List<MoneyData>> {
                override fun onResponse(
                    call: Call<List<MoneyData>>,
                    response: Response<List<MoneyData>>
                ) {
                    moneyAdapter.moneyList = response.body() as MutableList<MoneyData>
                    Log.d("moneyList", "${response.body()}")
                    moneyAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<List<MoneyData>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        // 추가
        binding.btnMoneyAdd.setOnClickListener {

            var addDialog = CustomMoneyBinding.inflate(layoutInflater)
            AlertDialog.Builder(requireContext()).run {
                setTitle("예산 카테고리 추가하기")
                setView(addDialog.root)

                setPositiveButton("확인", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        var m = MoneyData(
                            0,
                            tId,
                            addDialog.edtTitle.text.toString(),
                            parseLong(addDialog.edtMoney.text.toString()) // long형으로 변환
                        )
                        SubClient.retrofit.insertMoney(tId, m)
                            .enqueue(object : retrofit2.Callback<MoneyData> {
                                override fun onResponse(
                                    call: Call<MoneyData>, response: Response<MoneyData>
                                ) {
                                    response.body()?.let { it1 -> moneyAdapter.insertMoney(it1) }
                                }

                                override fun onFailure(call: Call<MoneyData>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }
                            })// enqueue
                    } // onclick
                }) // positive

                setNegativeButton("취소", null)
                show()
            }
        } // btnMoneyAdd


        var payList = mutableListOf<PayData>()

//        // 메인에서 받은 travId로 페이 목록 불러와서 총 지출 계산하면 됨


        SubClient.retrofit.findTravIdPayList(tId)
            .enqueue(object : retrofit2.Callback<List<PayData>> {
                override fun onResponse(call: Call<List<PayData>>, response: Response<List<PayData>>){
                    var totalMinus = 0
                    payList = response.body() as MutableList<PayData>
                    Log.d("payListResponse", "${response.body()}")
                    for (i in payList) {
                        // 지출만 골라내기
                        if ((i.plusAmt)!!.toInt() == 0) {
                            totalMinus += i.minusAmt!!.toInt()
                        }
                    }
                    // 총지출 데이터 갱신이 안됨 디비에서 데이터 바뀌면 갱신되게 해야함
                    binding.totalPay.text = "총지출 : ${DecimalFormat("#,###").format(totalMinus)}원"
                }

                override fun onFailure(call: Call<List<PayData>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        moneyAdapter.onItemChangeListener = object:MoneyAdapter.OnItemChangeListener{
            override fun onItemChange() {
                SubClient.retrofit.findTravIdPayList(tId)
                    .enqueue(object : retrofit2.Callback<List<PayData>> {
                        override fun onResponse(call: Call<List<PayData>>,response: Response<List<PayData>>){
                            var totalMinus = 0
                            payList = response.body() as MutableList<PayData>
                            Log.d("payListResponse", "${response.body()}")
                            for (i in payList) {
                                // 지출만 골라내기
                                if ((i.plusAmt)!!.toInt() == 0) {
                                    totalMinus += i.minusAmt!!.toInt()
                                }
                            }
                            // 총지출 데이터 갱신이 안됨 디비에서 데이터 바뀌면 갱신되게 해야함
                            binding.totalPay.text = "총지출 : ${DecimalFormat("#,###").format(totalMinus)}원"
                        }

                        override fun onFailure(call: Call<List<PayData>>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
            }

        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
//        var intent = Intent(context, MainActivity::class.java)
//        startActivity(intent)
    }

}



