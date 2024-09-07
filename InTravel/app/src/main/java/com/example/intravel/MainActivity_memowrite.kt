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
import java.util.Locale

class MainActivity_memowrite : AppCompatActivity() {

    var selectedDate:String = ""

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

        // 캘린더 클릭시 다이얼로그
        binding.btnMemoDate.setOnClickListener {
            val dialogMemo = CustomMemodateBinding.inflate(layoutInflater)

            AlertDialog.Builder(this).run {
                setTitle("여행 날짜 선택")
                setView(dialogMemo.root)
                // 날짜 선택시 여행 시작날 - 종료 날만 활성화 -> 구현 추후에 하기


                // 캘린더 날짜 선택 시 하부에 선택된 날짜 표현
                dialogMemo.memoCalendarView.setOnDateChangeListener { calendarView, year, month, date  ->
                    var m_month = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
                    var m_date = if (date < 10) "0${date}" else "$date"
                    selectedDate = "$year.$m_month.$m_date"  // YYYY.MM.DD 형식
                    dialogMemo.choiceDate.setText(selectedDate)
                }

                setPositiveButton("확인", object :DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        intent.putExtra("choiceDate", selectedDate)
                    }
                })
                setNegativeButton("취소", null)
                show()
            }
        }

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
            }
        }

        // 확인 버튼 클릭
        binding.btnMemoOk.setOnClickListener {
            val buttonType = if (binding.btnMemoOk.text == "UPDATE") "update" else "add"
            intent.putExtra("button", buttonType)
            intent.putExtra("mTitle", binding.mTitle.text.toString())
            intent.putExtra("mContent", binding.mContent.text.toString())
            intent.putExtra("choiceDate", selectedDate)  // 선택된 날짜 (다이얼로그에서 선택한 날짜)

            setResult(RESULT_OK, intent)
            finish()
        }

        // 취소 버튼 클릭
        binding.btnMemoCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.btnMemoDelete.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("삭제")
                setMessage("정말로 이 메모를 삭제하시겠습니까?")
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