package com.example.intravel

import android.content.DialogInterface
import android.icu.util.Calendar
import android.icu.util.TimeZone.SystemTimeZoneType
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.adapter.MainAdapter
import com.example.intravel.data.MainData
import com.example.intravel.databinding.ActivityMainBinding
import com.example.intravel.databinding.CustomDdayBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.time.Duration.Companion.days

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
//    setContentView(R.layout.activity_main)
    val binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }
    // 데이터 생성
    val mainList = mutableListOf<MainData>()

    // 어댑터 생성
    val mainAdapter = MainAdapter(mainList)
    binding.recyclerViewMain.adapter = mainAdapter
    binding.recyclerViewMain.layoutManager = LinearLayoutManager(this)

    // 오늘 날짜 시스템에서 받아오기
    val format = "yyyy.MM.dd." // 포맷 설정
    val today = Date(System.currentTimeMillis()) // 현재 시스템 날짜
    val dateFormat = SimpleDateFormat(format) // 포맷 적용해서 저장

    // 한글 요일 구하는 함수
    fun doDayOfWeek():String?{
      val cal: Calendar = Calendar.getInstance()
      var strWeek: String? = null
      val nWeek: Int = cal.get(Calendar.DAY_OF_WEEK)

      if (nWeek == 1) {
        strWeek = "일요일"
      } else if (nWeek == 2) {
        strWeek = "월요일"
      } else if (nWeek == 3) {
        strWeek = "화요일"
      } else if (nWeek == 4) {
        strWeek = "수요일"
      } else if (nWeek == 5) {
        strWeek = "목요일"
      } else if (nWeek == 6) {
        strWeek = "금요일"
      } else if (nWeek == 7) {
        strWeek = "토요일"
      }
      return strWeek
    }

    // 한글로 요일 받기
    val korDate:String? = doDayOfWeek()
    // 날짜 포맷 맞춰서 받기
    val todayDate :String = dateFormat.format(today)

    binding.titleToday.text = todayDate
    binding.titleDate.text = korDate

    // 데이터 추가
    // + 버튼 누르면 값 디비에 추가 후 리스폰된 값 받아서 화면에 띄움
    // Y 진행중, N 완료됨
    mainList.add(MainData(0,"일본 여행",todayDate,"2024.08.14","2024.08.20","동생",-6,'Y'))
    mainList.add(MainData(0,"경주 여행",todayDate,"2024.08.14","2024.08.20","친구",-13,'Y'))
    mainList.add(MainData(0,"전주 여행",todayDate,"2024.08.14","2024.08.20","동생",+2,'Y'))

    // 전체보기
    // 데이터 리스트 불러와서 리사이클러뷰에 붙이기
    binding.btnList.setOnClickListener { 
      
    }

    // 진행중 *첫화면*
    // cate 의 값이 Y 인 것만 불러오기
    binding.btnIng.setOnClickListener {
      for (i in 1..mainList.size){
        if(mainList.get(i).tComplete == 'Y'){
          // 새 리스트에 넣고 어댑터에 연결함
        }
      }
    }

    // 완료
    // cate 의 값이 N인 것만 불러오기
    binding.butEnd.setOnClickListener { 
      
    }

    // 카테고리 데이터
//    val cateList = listOf("혼자","친구","가족","연인")
    val cateData = resources.getStringArray(R.array.cateList)
    var cateSelected:String?=null

    // 추가하기 다이얼로그
    // 제목, 출발, 마감, 카테고리
    binding.btnAdd.setOnClickListener { 
      val dialogInsert = CustomDdayBinding.inflate(layoutInflater)

      // spinner 드롭다운 어댑터
      val cateAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,cateData)
      dialogInsert.cateSpinner.adapter = cateAdapter

      AlertDialog.Builder(this).run{
        setTitle("디데이 추가하기")
        setMessage("날짜를 선택해주세요")
        setView(dialogInsert.root)
        // 날짜 선택해도 오늘 날짜만 활성화 > edt 선택 안하면 날짜 선택 불가
        dialogInsert.calendarView.setOnDateChangeListener{calendarView, year, month, date ->
          dialogInsert.calendarView.setDate(System.currentTimeMillis())
        }

        // 출발 날짜를 눌렀을 때 캘린더 활성화
        dialogInsert.edtStart.setOnClickListener {
          dialogInsert.calendarView.setOnDateChangeListener { calendarView, year, month, date ->
            dialogInsert.edtStart.setText("${year}.${month+1}.${date}")
          }
        }

        // 마감 날짜를 눌렀을 때 캘린더 활성화
        dialogInsert.edtEnd.setOnClickListener {
          dialogInsert.calendarView.setOnDateChangeListener { calendarView, year, month, date ->
            dialogInsert.edtEnd.setText("${year}.${month+1}.${date}")
          }
        }

        dialogInsert.cateSpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
          override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            cateSelected = cateData.get(position)
          }

          override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
          }

        }
        // 어댑터 온클릭으로 열리면 수정 버튼으로 바뀌게
        setPositiveButton("확인",object:DialogInterface.OnClickListener{
          override fun onClick(p0: DialogInterface?, p1: Int) {
            mainList.add(MainData(0, dialogInsert.ddayName.text.toString(),todayDate,dialogInsert.edtStart.text.toString(),dialogInsert.edtEnd.text.toString(),cateSelected,-2,'Y'))
            mainAdapter.notifyDataSetChanged()
          }
        })

        setNegativeButton("취소",null)
        show()
      }//dialog
    }// btnAdd

    // 인텐트로 서브창 넘어가게 하기 데이터 가지고 갈 필요 X
    // 어댑터에 온클릭 인터페이스 생성


  }// onCreate

}