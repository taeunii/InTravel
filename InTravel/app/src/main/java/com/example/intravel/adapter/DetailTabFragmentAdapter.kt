package com.example.intravel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.intravel.Fragment.MoneyTabFragment
import com.example.intravel.fragments.GalleryFragment
import com.example.intravel.fragments.TodoListFragment
import com.example.intravel.fragments.MemoFragment


class DetailTabFragmentAdapter(private val fragmentActivity: FragmentActivity)
  :FragmentStateAdapter(fragmentActivity) {
  var fragments = listOf<Fragment>(TodoListFragment(), MemoFragment(), MoneyTabFragment(), GalleryFragment())
  override fun getItemCount(): Int {
    return  fragments.size
  }

  override fun createFragment(position: Int): Fragment {
    return fragments[position]
  }
}