package com.example.intravel


import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.intravel.adapter.DetailTabFragmentAdapter


import com.example.intravel.databinding.ActivitySubmainBinding
import com.example.intravel.databinding.FragmentGalleryBinding

import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class DetailMainActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySubmainBinding
//  private lateinit var viewPager2Adapter: MyFragmentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // View binding setup
    binding = ActivitySubmainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tablayout)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.right, systemBars.top, systemBars.bottom)
      insets
    }

    var tId = intent.getLongExtra("tId", 0)
    var tTitle = intent.getStringExtra("tTitle")
    var dday = intent.getStringExtra("dday")
    var today = intent.getStringExtra("today")

    binding.headerTitle.text = tTitle
    binding.mainTitle2.text = dday
    binding.mainSubtitle.text = today


    // MyFragmentAdapter 설정
    val detailTabFragmentAdapter = DetailTabFragmentAdapter(this)
    binding.viewpager2.adapter = detailTabFragmentAdapter

    val tabElement: List<String> = mutableListOf("To-Do", "Memo", "Gallery")


    try {
      TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
        val textView = TextView(this@DetailMainActivity)
        textView.text = tabElement[position]
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        tab.customView = textView
//        tab.text. = View.TEXT_ALIGNMENT_CENTER
      }.attach()
    } catch (e: Exception) {
      Log.e("TabLayoutError", "Error in TabLayoutMediator: ${e.message}")
    }
  }
}