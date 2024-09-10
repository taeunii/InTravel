package com.example.intravel.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.data.TempData
import com.example.intravel.databinding.ItemBinding


class DetailViewPagerAdapter(var listData:MutableList<TempData>): RecyclerView.Adapter<DetailViewPagerAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    // 가장 먼저 실행됨
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // 뷰 홀더 생성하고 여기서 하나씩 붙임
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tab = listData[position]

//        holder.binding.tvDate1.id

//        holder.

        // 배경색 설정 (여기서는 Tab 객체에 배경 정보가 없으므로 임의의 색상 사용)
//        holder.binding.recyclerLayout.setBackgroundColor(tab.color)

    }
}