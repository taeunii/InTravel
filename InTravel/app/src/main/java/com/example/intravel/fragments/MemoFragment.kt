package com.example.intravel.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.adapter.MemoAapter
import com.example.intravel.client.SubClient
import com.example.intravel.data.Memo
import com.example.intravel.databinding.CustomMemodateBinding
import com.example.intravel.databinding.CustomMemowriteBinding
import com.example.intravel.databinding.FragmentMemoBinding
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MemoFragment : Fragment() {

    private lateinit var binding: FragmentMemoBinding
    private lateinit var memoAdapter: MemoAapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tId = activity?.intent?.getLongExtra("tId", 0) ?: 0
        val tStartDate = activity?.intent?.getStringExtra("tStartDate")
        val tEndDate = activity?.intent?.getStringExtra("tEndDate")
        val tComplete = activity?.intent?.getCharExtra("travComplete",'N')

        // StartDate와 EndDate를 Date 객체로 변환
        val startDate: Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(tStartDate) ?: Date()
        val endDate: Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(tEndDate) ?: Date()

        var selectedDate:String = ""

        // 데이터 및 어댑터 생성, 리사이클러뷰 연결
        val memoList = mutableListOf<Memo>()
        memoAdapter = MemoAapter(memoList, tStartDate, tEndDate, tComplete)
        binding.memoRecyclerView.adapter = memoAdapter
        binding.memoRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        // 서버에서 memo 목록 가져오기
        SubClient.retrofit.findAllMemo(tId).enqueue(object :retrofit2.Callback<List<Memo>> {
            override fun onResponse(call: Call<List<Memo>>, response: Response<List<Memo>>) {
                response.body()?.let {
                    memoAdapter.memoList = it.toMutableList()
                    memoAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<List<Memo>>, t: Throwable) {
            }
        })  //findAllMemo


        // 완료일 지나면 작성 못하게
        // complete Y 이면 비활성화

        if(tComplete == 'Y'){
            binding.btnMemoAdd.isVisible = false
        }
        else{
            binding.btnMemoAdd.isEnabled = true
        }
        // 메모 추가 버튼 (다이얼로그 창)
        binding.btnMemoAdd.setOnClickListener {
            val addDialog = CustomMemowriteBinding.inflate(layoutInflater)
            val memoWriteDialog = CustomMemodateBinding.inflate(layoutInflater)

            AlertDialog.Builder(requireContext()).run {
                setTitle("여행 메모 추가")
                setView(addDialog.root)

                // 캘린더 클릭시 다이얼로그
                addDialog.btnMemoDate.setOnClickListener {
                    AlertDialog.Builder(requireContext()).run {
                        setTitle("여행 날짜 선택")
                        setView(memoWriteDialog.root)

                        // 캘린더에서 선택 가능한 날짜 범위 설정
                        memoWriteDialog.memoCalendarView.minDate = startDate.time
                        memoWriteDialog.memoCalendarView.maxDate = endDate.time

                        if (selectedDate.isNotEmpty()) {
                            val dateParts = selectedDate.split(".")
                            if (dateParts.size == 3) {
                                val year = dateParts[0].toInt()
                                val month = dateParts[1].toInt() - 1
                                val day = dateParts[2].toInt()

                                val calendar = Calendar.getInstance()
                                calendar.set(year, month, day)
                                memoWriteDialog.memoCalendarView.date = calendar.timeInMillis
                            }
                        }

                        // 캘린더 날짜 선택 시 하부에 선택된 날짜 표현
                        memoWriteDialog.memoCalendarView.setOnDateChangeListener { calendarView, year, month, date  ->
                            var m_month = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
                            var m_date = if (date < 10) "0${date}" else "$date"
                            selectedDate = "$year.$m_month.$m_date"  // YYYY.MM.DD 형식
                            memoWriteDialog.choiceDate.setText(selectedDate)
                        }

                        setPositiveButton("확인", object :DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                if (selectedDate.isEmpty()) {
                                    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                                    selectedDate = dateFormat.format(Date())
                                }
                                addDialog.MemoDate.setText(selectedDate)
                            }
                        })
                        setNegativeButton("취소", null)
                        show()
                    }
                }   //btnMemoDate

                setPositiveButton("확인", object :DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val memoItem = Memo(0,
                            tId,
                            addDialog.edtTitle.text.toString(),
                            addDialog.edtContent.text.toString(),
                            addDialog.MemoDate.text.toString(),
                            "")
                        SubClient.retrofit.insertMemo(tId, memoItem).enqueue(object :retrofit2.Callback<Memo> {
                            override fun onResponse(call: Call<Memo>, response: Response<Memo>) {
//                                Log.d("memo save","${response.body()}")
                                response.body()?.let { item -> memoAdapter.addMemo(item) }
                            }
                            override fun onFailure(call: Call<Memo>, t: Throwable) {
                            }
                        })
                    }
                })  //setPositiveButton
                setNegativeButton("취소", null)
                show()
            }
        }
    }
}