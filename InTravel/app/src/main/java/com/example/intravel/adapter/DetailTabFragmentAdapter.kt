package com.example.intravel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.intravel.fragments.TodoListFragment
import com.example.intravel.fragments.DetailTab3Fragment
import com.example.intravel.fragments.MemoFragment


class DetailTabFragmentAdapter(private val fragmentActivity: FragmentActivity)
    :FragmentStateAdapter(fragmentActivity) {
    var fragments = listOf<Fragment>(TodoListFragment(), MemoFragment(), DetailTab3Fragment())
    override fun getItemCount(): Int {
        return  fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}