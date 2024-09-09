package com.example.intravel.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.DetailMainActivity
import com.example.intravel.R
import com.example.intravel.adapter.MoneyAdapter
import com.example.intravel.adapter.TodoListAdapter
import com.example.intravel.data.MoneyData
import com.example.intravel.databinding.FragmentMoneyBinding
import com.example.intravel.databinding.FragmentTodoListBinding


class MoneyTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMoneyBinding.inflate(inflater, container, false)
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

        moneyAdapter.onItemClickListener =object:MoneyAdapter.OnItemClickListener{
            override fun onItemClick(money: MoneyData, position: Int) {
               

            }
        }



        return binding.root
    }

    }



