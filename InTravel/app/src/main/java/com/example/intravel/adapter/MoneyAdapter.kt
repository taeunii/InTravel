package com.example.intravel.adapter

import android.app.PendingIntent.getActivity
import android.content.ClipData.Item
import android.content.Intent
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.DetailMainActivity
import com.example.intravel.Fragment.TodoListFragment
import com.example.intravel.MainActivity
import com.example.intravel.data.MoneyData
import com.example.intravel.databinding.ItemMoneyBinding


class MoneyAdapter(var moneyList:MutableList<MoneyData>):RecyclerView.Adapter<MoneyAdapter.Holder>() {
    class Holder(val binding: ItemMoneyBinding):RecyclerView.ViewHolder(binding.root) {

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
        holder.binding.mMinus.text = "지출 : 90.000원"// pay화면에서 인텐트로 가져와야할 뜻 >>  인텐트가 안된대

        holder.itemView.setOnClickListener {
            onItemClickListener!!.onItemClick(money,position)
        }
    }


    override fun getItemCount(): Int {
        return moneyList.size
    }
}