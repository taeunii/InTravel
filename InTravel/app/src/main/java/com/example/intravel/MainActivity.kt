package com.example.intravel


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.intravel.adapter.MyFragmentAdapter


import com.example.intravel.databinding.ActivityMainBinding

import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
//  private lateinit var viewPager2Adapter: MyFragmentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)



    // View binding setup
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)


    // MyFragmentAdapter 설정
    val viewPager2Adapter = MyFragmentAdapter(this)
    binding.viewpager2.adapter = viewPager2Adapter

    val tabElement: List<String> = mutableListOf("To-Do", "Memo", "Menu")


    try {
      TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
        val textView = TextView(this@MainActivity)
        textView.text = tabElement[position]
        tab.customView = textView
//        tab.text. = View.TEXT_ALIGNMENT_CENTER
      }.attach()
    } catch (e: Exception) {
      Log.e("TabLayoutError", "Error in TabLayoutMediator: ${e.message}")
    }
  }
}