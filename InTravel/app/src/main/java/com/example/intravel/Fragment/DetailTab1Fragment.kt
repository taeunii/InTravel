package com.example.intravel.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.adapter.TabOneApapter
import com.example.intravel.databinding.FragmentTab1Binding

class DetailTab1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentTab1Binding.inflate(inflater, container, false)
        binding.recyclerView.adapter = TabOneApapter(listOf("test1", "test2", "test3"))

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

       // return inflater.inflate(R.layout.fragment_tab1, container, false)
        return binding.root
    }
}