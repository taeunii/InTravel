package com.example.intravel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.adapter.MemoAapter
import com.example.intravel.client.SubClient
import com.example.intravel.data.Memo
import com.example.intravel.data.TodoList
import com.example.intravel.databinding.ActivityMainMemoBinding
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity_memo : AppCompatActivity() {

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

        var tId = intent.getLongExtra("tId",0)
        var tStartDate = intent.getStringExtra("tStartDate")
        var tEndDate = intent.getStringExtra("tEndDate")
        val tComplete = intent?.getCharExtra("travComplete",'N')

        // 데이터 및 어댑터 생성, 리사이클러뷰 연결
        val memoList = mutableListOf<Memo>()
//        val memoAdapter = MemoAapter(memoList)
        val memoAdapter = MemoAapter(memoList, tStartDate, tEndDate, tComplete)
        binding.memoRecyclerView.adapter = memoAdapter
        binding.memoRecyclerView.layoutManager = LinearLayoutManager(this)


        // 전체 memolist 보기 (바로 화면에 보여야함)
        // DB 연결용
        SubClient.retrofit.findAllMemo(tId).enqueue(object :retrofit2.Callback<List<Memo>> {
            override fun onResponse(call: Call<List<Memo>>, response: Response<List<Memo>>) {
                if (response.body() != null) {
                    memoAdapter.memoList = response.body() as MutableList<Memo>
                    memoAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<List<Memo>>, t: Throwable) {
            }
        })  //findAllMemo


        // 메모 작성 페이지에서 받아온 데이터
        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val mId = it.data?.getLongExtra("mId", 0)?:0
                val pos = it.data?.getIntExtra("pos", 0)?:0
                val button = it.data?.getStringExtra("button")

                val mTitle = it.data?.getStringExtra("mTitle") ?: ""
                val mContent = it.data?.getStringExtra("mContent") ?: ""
                val choiceDate = it.data?.getStringExtra("choiceDate") ?: ""

                // 받아온 데이터를 리스트에 추가
                var newMemo = Memo(0, tId, mTitle, mContent, "", choiceDate)
                if (button == "add") {  // 추가
                    SubClient.retrofit.insertMemo(tId, newMemo).enqueue(object :retrofit2.Callback<Memo> {
                        override fun onResponse(call: Call<Memo>, response: Response<Memo>) {
                            if (response.body() != null) {
                                memoAdapter.addMemo(response.body()!!)
                            }
                        }
                        override fun onFailure(call: Call<Memo>, t: Throwable) {
                        }
                    })  //insertMemo
                }
                else if (button == "update") {  // 수정
                    SubClient.retrofit.updateMemo(mId, newMemo).enqueue(object :retrofit2.Callback<Memo> {
                        override fun onResponse(call: Call<Memo>, response: Response<Memo>) {
                            if (response.body() != null) {
                                memoAdapter.updateMemo(newMemo, pos!!)
                            }
                        }
                        override fun onFailure(call: Call<Memo>, t: Throwable) {
                        }
                    })  //updateMemo
                }
                else if (button == "delete") {  // 삭제
                    SubClient.retrofit.deleteByIdMemo(mId).enqueue(object :retrofit2.Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.body() != null) {
                                memoAdapter.removeMemo(pos!!)
                            }
                        }
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                        }
                    })  //deleteByIdMemo
                }
                else if (it.resultCode == RESULT_CANCELED) {
                }
            }
        }

        // 추가 버튼 클릭
        binding.btnMemoAdd.setOnClickListener {
            val intent = Intent(this@MainActivity_memo, MainActivity_memowrite::class.java)
            intent.putExtra("button", "add")
            intent.putExtra("tStartDate", tStartDate)
            intent.putExtra("tEndDate", tEndDate)
            activityResultLauncher.launch(intent)
        }

        // 수정
        memoAdapter.onItemClickListener = object :MemoAapter.OnItemClickListener {
            override fun onItemClick(memo: Memo, pos: Int) {
                val memo = memoAdapter.memoList[pos]
                val intent = Intent(this@MainActivity_memo, MainActivity_memowrite::class.java)
                intent.putExtra("mId", memo.memoId)
                intent.putExtra("mTitle", memo.memoTitle)
                intent.putExtra("mContent", memo.memoContent)
                intent.putExtra("choiceDate", memo.choiceDate)
                // intent.putExtra("mCreateDate", memo.memoCreateDate)

                intent.putExtra("pos", pos)
                intent.putExtra("button", "update")
                activityResultLauncher.launch(intent)
            }
        }
    }
}