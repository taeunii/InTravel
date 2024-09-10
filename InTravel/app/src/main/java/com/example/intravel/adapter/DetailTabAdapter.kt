package com.example.intravel.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.databinding.ItemBinding

class DetailTabAdapter(private val items: List<String>) : RecyclerView.Adapter<DetailTabAdapter.Holder>() {

  inner class Holder(val binding:ItemBinding) : RecyclerView.ViewHolder(binding.root){

    fun ItemInit(item:String){
      binding.tvTitle1.text = item
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
    return Holder(
      ItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false))
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(holder: Holder, position: Int) {
    holder.ItemInit(items[position])
    Log.d("onBindViewHolder :" , items[position])

  }


}