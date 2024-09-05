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
import com.example.intravel.data.Memo
import com.example.intravel.databinding.ActivityMainMemoBinding

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

        val memoList = mutableListOf<Memo>()
        val memoAdapter = MemoAapter(memoList)
        binding.memoRecyclerView.adapter = memoAdapter
        binding.memoRecyclerView.layoutManager = LinearLayoutManager(this)

        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val mTitle = it.data?.getStringExtra("mTitle").toString()?:""
                val mContent = it.data?.getStringExtra("mContent").toString()?:""
                memoList.add(Memo(0, mTitle, mContent))
                memoAdapter.notifyDataSetChanged()
            }
        }

        binding.btnMemoAdd.setOnClickListener {
            val intent = Intent(this@MainActivity_memo, MainActivity_memowrite::class.java)
            activityResultLauncher.launch(intent)
        }

        memoAdapter.onItemClickListener = object :MemoAapter.OnItemClickListener {
            override fun onItemClick(memo: Memo, pos: Int) {
                val memo = memoAdapter.memoList[pos]

            }
        }
    }
}