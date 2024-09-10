package com.example.intravel.Fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.DetailMainActivity
import com.example.intravel.MainActivity
import com.example.intravel.R
import com.example.intravel.TestActivity
import com.example.intravel.adapter.MoneyAdapter
import com.example.intravel.client.SubClient
import com.example.intravel.data.MoneyData
import com.example.intravel.databinding.CustomMoneyBinding
import com.example.intravel.databinding.FragmentMoneyBinding
import com.example.intravel.databinding.FragmentTodoListBinding
import retrofit2.Call
import retrofit2.Response
import java.lang.Long.parseLong


class MoneyTabFragment : Fragment() {

    private lateinit var binding: FragmentMoneyBinding
    private lateinit var moneyAdapter: MoneyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMoneyBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.moneyRecyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 데이터 생성
        var moneyList = mutableListOf<MoneyData>(
            MoneyData(0,0,"교통비",100000),
            MoneyData(0,0,"식비",300000)
        )
        // 어댑터 생성
        var moneyAdapter = MoneyAdapter(moneyList)
        // 리사이클러뷰 어댑터 연결
        binding.moneyRecyclerView.adapter = moneyAdapter
        // 레이아웃 설정
        binding.moneyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tId = activity?.intent?.getLongExtra("tId", 0) ?: 0

        // 추가
        binding.btnMoneyAdd.setOnClickListener{
            var addDialog = CustomMoneyBinding.inflate(layoutInflater)
            AlertDialog.Builder(requireContext()).run{
                setTitle("예산 카테고리 추가하기")
                setView(addDialog.root)

                setPositiveButton("확인",object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        var m = MoneyData(0,
                            tId,
                            addDialog.edtTitle.text.toString(),
                            parseLong(addDialog.edtMoney.text.toString()) // long형으로 변환
                        )
                        SubClient.retrofit.insertMoney(tId,m).enqueue(object:retrofit2.Callback<MoneyData>{
                            override fun onResponse(
                                call: Call<MoneyData>,response: Response<MoneyData>) {
                                response.body()?.let { it1 -> moneyAdapter.insertMoney(it1) }
                            }

                            override fun onFailure(call: Call<MoneyData>, t: Throwable) {
                                TODO("Not yet implemented")
                            }

                        })
                    }

                })
                setNegativeButton("취소",null)
                show()
            }
        }



        moneyAdapter.onItemClickListener =object:MoneyAdapter.OnItemClickListener{
            override fun onItemClick(money: MoneyData, position: Int) {

                // 다이얼로그창은 뜸
                // money 항목 추가는 다이얼로그로 해도 pay 내역 추가하고 목록 보려면 다른창이 있어야하긴 함
            }
        }

    }

    }



