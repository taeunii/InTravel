package com.example.intravel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.intravel.Tab1Fragment
import com.example.intravel.Tab2Fragment
import com.example.intravel.Tab3Fragment


class MyFragmentAdapter(private val fragmentActivity: FragmentActivity)
    :FragmentStateAdapter(fragmentActivity) {
    var fragments = listOf<Fragment>(Tab1Fragment(), Tab2Fragment(), Tab3Fragment())
    override fun getItemCount(): Int {
        return  fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}