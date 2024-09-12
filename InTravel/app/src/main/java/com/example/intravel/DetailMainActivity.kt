package com.example.intravel


import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.viewpager2.widget.ViewPager2
import com.example.intravel.Fragment.MoneyTabFragment
import com.example.intravel.adapter.DetaiTabFragmentAdapter
import com.example.intravel.adapter.DetailViewPagerAdapter


import com.example.intravel.databinding.ActivitySubmainBinding

import com.google.android.material.tabs.TabLayoutMediator


class DetailMainActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySubmainBinding
//  private lateinit var viewPager2Adapter: MyFragmentAdapter
  lateinit var viewPager2Adapter: DetaiTabFragmentAdapter
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // View binding setup
    binding = ActivitySubmainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tablayout)) {v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.right, systemBars.top, systemBars.bottom)
      insets
    }

    var tId = intent.getLongExtra("tId",0)
    var tTitle = intent.getStringExtra("tTitle")
    var dday = intent.getStringExtra("dday")
    var today = intent.getStringExtra("today")

    // 완료된 아이템은 디데이말고 기한으로 보이게 하기
    var travComplete = intent.getCharExtra("travComplete",'a')
    var tStartDate = intent.getStringExtra("tStartDate")
    var tEndDate = intent.getStringExtra("tEndDate")

    binding.headerTitle.text = tTitle
    binding.mainTitle2.text = "D $dday"
    binding.mainSubtitle.text = today

    var sYear = tStartDate!!.substring(0,4) // 2024
    var sMonth = tStartDate!!.substring(4,6) // 09
    var sDay = tStartDate!!.substring(6) // 01

    var eYear = tEndDate!!.substring(0,4) // 2024
    var eMonth = tEndDate!!.substring(4,6) // 09
    var eDay = tEndDate!!.substring(6) // 01


    if(travComplete == 'Y'){
      binding.frontLayout.isVisible = false
      binding.compText.isVisible=true
      binding.compText.text = "$sYear.$sMonth.$sDay~$eYear.$eMonth.$eDay"
    }


    // MyFragmentAdapter 설정
    viewPager2Adapter = DetaiTabFragmentAdapter(this)
    binding.viewpager2.adapter = viewPager2Adapter


    val tabElement: List<String> = mutableListOf("To-Do", "Memo", "Menu")


    try {
      TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
        val textView = TextView(this@DetailMainActivity)
        textView.text = tabElement[position]
        tab.customView = textView
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.setTypeface(textView.typeface, Typeface.BOLD)
//        tab.text. = View.TEXT_ALIGNMENT_CENTER
      }.attach()
    } catch (e: Exception) {
      Log.e("TabLayoutError", "Error in TabLayoutMediator: ${e.message}")
    }
  }


}