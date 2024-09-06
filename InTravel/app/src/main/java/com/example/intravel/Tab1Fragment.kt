package com.example.intravel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.RecyclerView.Adapter
import com.example.intravel.databinding.FragmentTab1Binding
import com.example.intravel.databinding.FragmentTab2Binding

class Tab1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentTab1Binding.inflate(inflater, container, false)

        binding.recyclerView.adapter = Adapter(listOf("test1", "test2", "test3"))

        return inflater.inflate(R.layout.fragment_tab1, container, false)
    }

}