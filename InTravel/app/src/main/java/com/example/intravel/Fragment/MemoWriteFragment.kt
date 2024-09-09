package com.example.intravel.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.R
import com.example.intravel.adapter.MemoAapter
import com.example.intravel.client.SubClient
import com.example.intravel.data.Memo
import com.example.intravel.databinding.CustomMemodateBinding
import com.example.intravel.databinding.FragmentMemoBinding
import com.example.intravel.databinding.FragmentMemoWriteBinding
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MemoWriteFragment : Fragment() {

    private lateinit var binding: FragmentMemoWriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemoWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tStartDate = activity?.intent?.getStringExtra("tStartDate")
        val tEndDate = activity?.intent?.getStringExtra("tEndDate")

        // SimpleDateFormat을 사용해 String -> Date 변환
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

        // StartDate 와 EndDate 를 Date 객체로 변환
        val start: Date = dateFormat.parse(tStartDate) ?: Date()
        val end: Date = dateFormat.parse(tEndDate) ?: Date()

        var selectedDate:String = ""
        val dialogMemo = CustomMemodateBinding.inflate(layoutInflater)

    }
}



