package com.example.intravel.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.R
import com.example.intravel.adapter.MemoAapter
import com.example.intravel.adapter.TodoListAdapter
import com.example.intravel.client.SubClient
import com.example.intravel.data.Memo
import com.example.intravel.data.TodoList
import com.example.intravel.databinding.FragmentMemoBinding
import com.example.intravel.databinding.FragmentTodoListBinding
import retrofit2.Call
import retrofit2.Response


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

        // 데이터 및 어댑터 생성, 리사이클러뷰 연결
        val memoList = mutableListOf<Memo>()
        memoAdapter = MemoAapter(memoList)
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

//        // 추가 버튼 클릭
//        binding.btnMemoAdd.setOnClickListener {
//            goMemoWrite()
//        }

    }

//    // 메모 작성 페이지로 이동
//    fun goMemoWrite() {
//        val memoWriteFragment = MemoWriteFragment()
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.memo_fragment_container, memoWriteFragment)
//            .addToBackStack(null)   // 뒤로가기 시 메모 목록으로 돌아오기 위해
//            .commit()
//    }
}