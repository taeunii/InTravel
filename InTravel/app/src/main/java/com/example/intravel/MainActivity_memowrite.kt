package com.example.intravel

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.intravel.client.SubClient
import com.example.intravel.databinding.ActivityMainMemowriteBinding
import com.example.intravel.databinding.CustomMemodateBinding
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity_memowrite : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainMemowriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var tStartDate = intent.getStringExtra("tStartDate")
        var tEndDate = intent.getStringExtra("tEndDate")

        // SimpleDateFormat을 사용해 String -> Date 변환
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

        // StartDate 와 EndDate 를 Date 객체로 변환
        val start: Date = dateFormat.parse(tStartDate) ?: Date()
        val end: Date = dateFormat.parse(tEndDate) ?: Date()

        var selectedDate:String = ""
        val dialogMemo = CustomMemodateBinding.inflate(layoutInflater)

        // "button" 에 따른 추가 또는 수정 작업 처리
        if (intent.getStringExtra("button").toString() == "add") {  // 추가
            binding.btnMemoOk.text = "OK"
            binding.btnMemoDelete.visibility = View.INVISIBLE   // 삭제 버튼 숨김
        }
        else {  // 수정
            with(binding) {
                btnMemoOk.text = "UPDATE"
                btnMemoDelete.visibility = View.VISIBLE   // 삭제 버튼 보이기
                mTitle.setText(intent.getStringExtra("mTitle"))
                mContent.setText(intent.getStringExtra("mContent"))

                // 선택된 날짜 가져오기
                val choiceDate = intent.getStringExtra("choiceDate") ?: ""

                // 가져온 날짜를 "YYYY.MM.DD" 형식에서 year, month, day 로 분리
                if (choiceDate.isNotEmpty()) {
                    val dateParts = choiceDate.split(".")
                    if (dateParts.size == 3) {
                        val year = dateParts[0].toInt()
                        val month = dateParts[1].toInt() - 1  // Calendar는 0부터 시작함
                        val day = dateParts[2].toInt()

                        // 캘린더 뷰를 선택한 날짜로 초기화
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, day)
                        dialogMemo.memoCalendarView.date = calendar.timeInMillis
                    }
                }
            }
        }

        // 캘린더 클릭시 다이얼로그
        binding.btnMemoDate.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("여행 날짜 선택")
                setView(dialogMemo.root)

                // 캘린더에서 선택 가능한 날짜 범위 설정
                dialogMemo.memoCalendarView.minDate = start.time  // tStartDate로 설정
                dialogMemo.memoCalendarView.maxDate = end.time    // tEndDate로 설정

                // 만약 선택된 날짜가 있으면 캘린더를 해당 날짜로 설정
                if (selectedDate.isNotEmpty()) {
                    val dateParts = selectedDate.split(".")
                    if (dateParts.size == 3) {
                        val year = dateParts[0].toInt()
                        val month = dateParts[1].toInt() - 1
                        val day = dateParts[2].toInt()

                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, day)
                        dialogMemo.memoCalendarView.date = calendar.timeInMillis
                    }
                }

                // 캘린더 날짜 선택 시 하부에 선택된 날짜 표현
                dialogMemo.memoCalendarView.setOnDateChangeListener { calendarView, year, month, date  ->
                    var m_month = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
                    var m_date = if (date < 10) "0${date}" else "$date"
                    selectedDate = "$year.$m_month.$m_date"  // YYYY.MM.DD 형식
                    dialogMemo.choiceDate.setText(selectedDate)
                }

                setPositiveButton("확인", object :DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        if (selectedDate.isEmpty()) {
                            val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                            val todayDate = dateFormat.format(Date())

                            selectedDate = todayDate
                        }

                        intent.putExtra("choiceDate", selectedDate)
                    }
                })
                setNegativeButton("취소", null)
                show()
            }
        }


        // 확인 버튼 클릭
        binding.btnMemoOk.setOnClickListener {
            when {
                binding.mTitle.text.toString().isEmpty() -> {
                    AlertDialog.Builder(this).apply {
//                        setTitle("경고")
                        setMessage("제목을 입력해주세요.")
                        setPositiveButton("확인", null)
                        show()
                    }
                }
                binding.mContent.text.toString().isEmpty() -> {
                    AlertDialog.Builder(this).apply {
//                        setTitle("경고")
                        setMessage("내용을 입력해주세요.")
                        setPositiveButton("확인", null)
                        show()
                    }
                }
                dialogMemo.choiceDate.text.toString().isEmpty() -> {
                    AlertDialog.Builder(this).apply {
//                        setTitle("경고")
                        setMessage("날짜를 선택해주세요.")
                        setPositiveButton("확인", null)
                        show()
                    }
                }
                else -> {
                    if (binding.btnMemoOk.text == "UPDATE") {
                        intent.putExtra("pos", intent.getIntExtra("pos", 0))
                        intent.putExtra("button", "update")
                    }
                    else {
                        intent.putExtra("button", "add")
                    }
                    intent.putExtra("mTitle", binding.mTitle.text.toString())
                    intent.putExtra("mContent", binding.mContent.text.toString())
                    intent.putExtra("choiceDate", selectedDate)  // 선택된 날짜 (다이얼로그에서 선택한 날짜)

                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }

        // 취소 버튼 클릭
        binding.btnMemoCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.btnMemoDelete.setOnClickListener {
            AlertDialog.Builder(this).apply {
//                setTitle("삭제")
                setMessage("삭제하시겠습니까?")
                setPositiveButton("삭제") { _, _ ->
                    val memoId = intent.getLongExtra("mId", 0)
                    if (memoId != 0L) {
                        SubClient.retrofit.deleteByIdMemo(memoId).enqueue(object :retrofit2.Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                intent.putExtra("button", "delete")
                                intent.putExtra("mId", memoId)
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                            }
                        })
                    }
                }
                setNegativeButton("취소", null)
            }.show()
        }
    }
}