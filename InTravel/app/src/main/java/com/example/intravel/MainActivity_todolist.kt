package com.example.intravel

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.adapter.TodoListAdapter
import com.example.intravel.client.SubClient
import com.example.intravel.data.TodoList
import com.example.intravel.databinding.ActivityMainTodolistBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//////// 추후 문제 없을시 삭제가능 (내용 TodoListFragment(Fragment 폴더) 로 이동함)

class MainActivity_todolist : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainTodolistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var tId = intent.getLongExtra("tId",0)

        // 데이터 및 어댑터 생성, 리사이클러뷰 연결
        val todoList = mutableListOf<TodoList>()
        val todoListAdapter = TodoListAdapter(todoList)
        binding.todoListRecyclerView.adapter = todoListAdapter
        binding.todoListRecyclerView.layoutManager = LinearLayoutManager(this)


        // 서버에서 todoList 가져오기
        SubClient.retrofit.findAllTodoList(tId).enqueue(object : Callback<List<TodoList>> {
            override fun onResponse(call: Call<List<TodoList>>, response: Response<List<TodoList>>) {
                response.body()?.let {
                    todoListAdapter.todoList = it.toMutableList()
                    todoListAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<List<TodoList>>, t: Throwable) {
            }
        })  //findAllTodoList

        // 새로운 TodoList 추가 버튼 이벤트 처리
        binding.btnTodoListAdd.setOnClickListener {
            val newTodo = TodoList(0, tId, "")

            // 서버에 새로운 TodoList 추가
            SubClient.retrofit.insertTodoList(tId, newTodo).enqueue(object : Callback<TodoList> {
                override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                    response.body()?.let { item -> todoListAdapter.addTodoList(item)}
                }
                override fun onFailure(call: Call<TodoList>, t: Throwable) {
                }
            })
        }




        // 전체 todolist 보기 (바로 화면에 보여야함)
        // DB 연결용
//        SubClient.retrofit.findAllTodoList(tId).enqueue(object :retrofit2.Callback<List<TodoList>> {
//            override fun onResponse(call: Call<List<TodoList>>, response: Response<List<TodoList>>) {
//                if (response.body() != null) {
//                    todoListAdapter.todoList = response.body() as MutableList<TodoList>
//                    todoListAdapter.notifyDataSetChanged()
//                }
////                todoListAdapter.todoList = response.body() as MutableList<TodoList>
////                todoListAdapter.notifyDataSetChanged()
//            }
//            override fun onFailure(call: Call<List<TodoList>>, t: Throwable) {
//            }
//        })  //findAllTodoList

        // 추가
        // DB 연결 전 테스트용 - 추후 삭제
//        binding.btnTodoListAdd.setOnClickListener {
//            todoListAdapter.todoList.add(TodoList(0, tId, "", 'N', 'N'))
//            todoListAdapter.notifyItemInserted(todoListAdapter.todoList.size - 1)
//        }   // 인덱스 0부터 시작하니까 크기는 -1
        // DB 연결용
//        binding.btnTodoListAdd.setOnClickListener {
//            val newTodo = TodoList(0, tId, "", 'N', 'N')
//
//            // 리사이클러뷰에 항목 먼저 추가
//            todoListAdapter.addTodoList(newTodo)
//
//            // 서버에 추가
//            SubClient.retrofit.insertTodoList(tId, newTodo).enqueue(object :retrofit2.Callback<TodoList> {
//                override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
//                    if (response.body() != null) {
//                        val index = todoListAdapter.todoList.indexOf(newTodo)
//                        if (index != -1) {
//                            todoListAdapter.todoList[index] = response.body()!!
//                            todoListAdapter.notifyItemChanged(index)
//                        }
//                    }
////                    todoListAdapter.addTodoList(response.body()!!)
//                }
//                override fun onFailure(call: Call<TodoList>, t: Throwable) {
//                }
//            })  //insertTodoList
//        }   //btnTodoListAdd

    }
}