package com.example.intravel

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.adapter.MemoAapter
import com.example.intravel.client.SubClient
import com.example.intravel.data.Memo
import com.example.intravel.databinding.ActivityMainMemoBinding
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity_memo : AppCompatActivity() {

    var currentTId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentTId = intent.getLongExtra("T_ID", 0) // 메인 화면에서 전달된 tId

        // 데이터 및 어댑터 생성, 리사이클러뷰 연결
        val memoList = mutableListOf<Memo>()
        val memoAdapter = MemoAapter(memoList)
        binding.memoRecyclerView.adapter = memoAdapter
        binding.memoRecyclerView.layoutManager = LinearLayoutManager(this)


        // 전체 memolist 보기 (바로 화면에 보여야함)
        // DB 연결용
        SubClient.retrofit.findAllMemo().enqueue(object :retrofit2.Callback<List<Memo>> {
            override fun onResponse(call: Call<List<Memo>>, response: Response<List<Memo>>) {
                memoAdapter.memoList.clear()
                memoAdapter.memoList = response.body() as MutableList<Memo>
                memoAdapter.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<List<Memo>>, t: Throwable) {
            }
        })  //findAllMemo


        // 메모 작성 페이지에서 받아온 데이터
        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val mId = it.data?.getLongExtra("mId", 0)?:0
                val pos = it.data?.getIntExtra("pos", 0)?:0
                val mTitle = it.data?.getStringExtra("mTitle")?:""
                val mContent = it.data?.getStringExtra("mContent")?:""
                val mCreateDate = it.data?.getStringExtra("mCreateDate")?:""
                val choiceDate = it.data?.getStringExtra("choiceDate")?:""

                // 받아온 데이터를 리스트에 추가
                var newMemo = Memo(0, currentTId, mTitle, mContent, mCreateDate, choiceDate)
                val button = it.data?.getStringExtra("button")
                if (button == "add") {  // 추가
                    SubClient.retrofit.insertMemo(newMemo).enqueue(object :retrofit2.Callback<Memo> {
                        override fun onResponse(call: Call<Memo>, response: Response<Memo>) {
                            memoAdapter.addMemo(response.body()!!)
                        }
                        override fun onFailure(call: Call<Memo>, t: Throwable) {
                        }
                    })  //insertMemo
                }
                else {  // 수정
                    SubClient.retrofit.updateMemo(mId, newMemo).enqueue(object :retrofit2.Callback<Memo> {
                        override fun onResponse(call: Call<Memo>, response: Response<Memo>) {
                            memoAdapter.updateMemo(newMemo, pos!!)
                        }
                        override fun onFailure(call: Call<Memo>, t: Throwable) {
                        }
                    })  //updateMemo
                }
            }
            else if (it.resultCode == RESULT_CANCELED) {
            }
        }

        // 추가 버튼 클릭 - 리턴 값 있음
        binding.btnMemoAdd.setOnClickListener {
            val intent = Intent(this@MainActivity_memo, MainActivity_memowrite::class.java)
            intent.putExtra("button", "add")
            activityResultLauncher.launch(intent)
        }

        // 수정, 삭제
        memoAdapter.onItemClickListener = object :MemoAapter.OnItemClickListener {
            override fun onItemClick(memo: Memo, pos: Int) {
                val memo = memoAdapter.memoList[pos]
                val intent = Intent(this@MainActivity_memo, MainActivity_memowrite::class.java)
                intent.putExtra("mId", memo.mId)
                intent.putExtra("mTitle", memo.mTitle)
                intent.putExtra("mContent", memo.mContent)
                intent.putExtra("mCreateDate", memo.mCreateDate)
                intent.putExtra("choiceDate", memo.choiceDate)

                intent.putExtra("pos", pos)
                intent.putExtra("button", "update")
                activityResultLauncher.launch(intent)
            }
        }
    }
}
