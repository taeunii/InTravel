package com.example.intravel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.intravel.Fragment.DetailTab1Fragment
import com.example.intravel.Fragment.DetailTab2Fragment
import com.example.intravel.Fragment.DetailTab3Fragment


class DetaiTabFragmentAdapter(private val fragmentActivity: FragmentActivity)
    :FragmentStateAdapter(fragmentActivity) {
    var fragments = listOf<Fragment>(DetailTab1Fragment(), DetailTab2Fragment(), DetailTab3Fragment())
    override fun getItemCount(): Int {
        return  fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}