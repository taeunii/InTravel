package com.example.intravel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intravel.data.PhotoData
import com.example.intravel.databinding.ItemGalleryBinding

class GalleryAdapter(var photoList: MutableList<PhotoData>): RecyclerView.Adapter<GalleryAdapter.Holder>() {
  inner class Holder(val binding: ItemGalleryBinding): RecyclerView.ViewHolder(binding.root) {

  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryAdapter.Holder {
    return Holder(ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }

  override fun onBindViewHolder(holder: GalleryAdapter.Holder, position: Int) {
    val photo = photoList[position]

    Glide.with(holder.binding.root.context)
      .load(photo.filePath+photo.fileName)
      .into(holder.binding.galleryIv)
  }

  override fun getItemCount(): Int {
    return photoList.size
  }
}