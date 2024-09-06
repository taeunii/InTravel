package com.example.intravel

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.intravel.databinding.ActivityMainMemowriteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity_memowrite : AppCompatActivity() {

    lateinit var binding: ActivityMainMemowriteBinding
    val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainMemowriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 캘린더 클릭시 선택 날짜 가져오기
        binding.btnDate.setOnClickListener {
            showDatePicker()
        }

        // 인텐트의 "button" 값에 따라 추가 또는 수정 작업 처리
        if (intent.getStringExtra("button").toString() == "add") {  // 추가
            binding.btnMemoOk.text = "OK"
            binding.btnMemoDelete.visibility = View.INVISIBLE   // 삭제 버튼 숨김
        }
        else {
            with(binding) {
                btnMemoOk.text = "UPDATE"
                btnMemoDelete.visibility = View.VISIBLE   // 삭제 버튼 보이기
                mTitle.setText(intent.getStringExtra("mTitle"))
                mContent.setText(intent.getStringExtra("mContent"))
                btnDate.text = intent.getStringExtra("choiceDate")
            }
        }

        // 추가 버튼 클릭
        binding.btnMemoOk.setOnClickListener {
            if (binding.btnMemoOk.text == "UPDATE") {
                intent.putExtra("mId", intent.getLongExtra("mId", 0))
                intent.putExtra("button", "update")
            }
            else {
                intent.putExtra("button", "add")
            }
            intent.putExtra("mTitle", binding.mTitle.text.toString())
            intent.putExtra("mContent", binding.mContent.text.toString())
            intent.putExtra("choiceDate", binding.btnDate.text.toString())

            setResult(RESULT_OK, intent)
            finish()
        }

        // 취소 버튼 클릭
        binding.btnMemoCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    // 추후에 startDate-EndDate 사이에 날짜만 선택할 수 있도록 해야함
    fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val dateFormat = SimpleDateFormat("yyyy. MM. dd", Locale.getDefault())
            binding.btnDate.text = dateFormat.format(selectedDate.time)
        }, year, month, day)

        datePicker.show()
    }


}