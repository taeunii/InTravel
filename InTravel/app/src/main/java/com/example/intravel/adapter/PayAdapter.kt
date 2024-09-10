package com.example.intravel.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.intravel.client.SubClient
import com.example.intravel.data.PayData
import com.example.intravel.databinding.ItemPayBinding
import retrofit2.Call
import retrofit2.Response

class PayAdapter(var context:Context,var payList: MutableList<PayData>):RecyclerView.Adapter<PayAdapter.Holder>() {
    inner class Holder(var binding: ItemPayBinding):RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayAdapter.Holder {
        return Holder(ItemPayBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PayAdapter.Holder, position: Int) {
        var pay = payList.get(position)
        Log.d("pay Item", "${payList[position]}")
        Log.d("pay position", "${position}")
        holder.binding.payTitle.setText(pay.payTitle)
        holder.binding.pMinus.setText(pay.minusAmt.toString())

    }

    override fun getItemCount(): Int {
        return payList.size
    }
}