package com.example.intravel.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.databinding.ItemBinding

class TabOneApapter(val tabOneList:List<String>)
    : RecyclerView.Adapter<TabOneApapter.Holer>() {
    class Holer(val binding: ItemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabOneApapter.Holer {
        return  Holer(ItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: TabOneApapter.Holer, position: Int) {
       holder.binding.tvTitle1.text = tabOneList[position]
        Log.d("onBindViewHolder" ,tabOneList[position] )
    }

    override fun getItemCount(): Int {
        Log.d("onBindViewHolder" ,tabOneList.size.toString() )
        return  tabOneList.size
    }
}