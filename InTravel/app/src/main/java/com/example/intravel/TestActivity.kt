package com.example.intravel

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.intravel.databinding.ActivityTestBinding
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.time.Duration.Companion.parse

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_test)
        val binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var a = parseInt("20240913")
        var b = parseInt("20240911")
        var result = b-a
//        var c = Calendar.getInstance()

//        var date = Date(System.currentTimeMillis()) // 현재 시스템 날짜
//        var dateFormat = SimpleDateFormat("yyyyMMdd")
//        val today :String = dateFormat.format(date)
//        var c = parseInt(today)
//        result = a-c

        // 음수인지 양수인지 구분해서 + - 붙이기
        // 음수면 -붙여서 양수로 만들기 , 기호가 보이면 안됨

        if(result < 0){
            result = -result
        }

        var dday = intent.getStringExtra("dday")
        var tTitle = intent.getStringExtra("tTitle")
        var tId = intent.getStringExtra("tId")
        var test = "tId:$tId, tTitle:$tTitle, dday:$dday"


        binding.txtView.text = test



    }
}