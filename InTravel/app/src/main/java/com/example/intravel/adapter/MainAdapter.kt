package com.example.intravel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.data.MainData
import com.example.intravel.databinding.ItemMainBinding

class MainAdapter(var mainList: MutableList<MainData>):RecyclerView.Adapter<MainAdapter.MainHolder>() {
    class MainHolder(var binding: ItemMainBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainHolder {
        return MainHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MainAdapter.MainHolder, position: Int) {
        val main = mainList.get(position)
        holder.binding.itemTitle.text = main.tTitle
        holder.binding.itemCate.text = main.cate
        //holder.binding.itemTextview.text = "D"
        holder.binding.itemDday.text = main.dday.toString()
    }

    override fun getItemCount(): Int {
        return mainList.size
    }
}