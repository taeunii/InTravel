package com.example.intravel.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.adapter.TodoListAdapter
import com.example.intravel.client.SubClient
import com.example.intravel.data.TodoList
import com.example.intravel.databinding.FragmentTodoListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding
    private lateinit var todoListAdapter: TodoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
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
        val todoList = mutableListOf<TodoList>()
        todoListAdapter = TodoListAdapter(todoList)
        binding.todoListRecyclerView.adapter = todoListAdapter
        binding.todoListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 서버에서 todoList 가져오기
        SubClient.retrofit.findAllTodoList(tId).enqueue(object : Callback<List<TodoList>> {
            override fun onResponse(call: Call<List<TodoList>>, response: Response<List<TodoList>>) {

                response.body()?.let { todos ->
                    // 완료 여부에 따라 정렬 (완료된 항목을 맨 아래로 이동)
                    val test1 = todos.sortedWith(compareBy({it.todoComplete == 'Y'}, {it.todoId}))

                    var test2 = mutableListOf<TodoList>()


                    for (item in test1) {
                        if (item.todoImpo == 'Y' && item.todoComplete=='N') {
                            test2.add(item)
                        }
                    }

                    for (item in test1) {
                        if (item.todoImpo == 'N' && item.todoComplete=='N') {
                            test2.add(item)
                        }
                    }

                    for (item in test1) {
                        if (item.todoImpo == 'Y'&& item.todoComplete=='Y') {
                            test2.add(item)
                        }
                    }

                    for (item in test1) {
                        if (item.todoImpo == 'N'&& item.todoComplete=='Y') {
                            test2.add(item)
                        }
                    }


                    // 어댑터에 정렬된 리스트 설정
                    todoListAdapter.todoList = test2.toMutableList()
                    todoListAdapter.notifyDataSetChanged()

                }
            }
            override fun onFailure(call: Call<List<TodoList>>, t: Throwable) {
            }
        })  //findAllTodoList

        // 데이터 업데이트되면 정렬 순서 맞춰서 화면 갱신
        todoListAdapter.onItemClickListener = object:TodoListAdapter.OnItemClickListener{
            override fun onItemClick() {
                SubClient.retrofit.findAllTodoList(tId).enqueue(object : Callback<List<TodoList>> {
                    override fun onResponse(call: Call<List<TodoList>>, response: Response<List<TodoList>>) {

                        response.body()?.let { todos ->
                            // 완료 여부에 따라 정렬 (완료된 항목을 맨 아래로 이동)
                            val test1 = todos.sortedWith(compareBy({it.todoComplete == 'Y'}, {it.todoId}))

                            var test2 = mutableListOf<TodoList>()

                            for (item in test1) {
                                if (item.todoImpo == 'Y' && item.todoComplete=='N') {
                                    test2.add(item)
                                }
                            }

                            for (item in test1) {
                                if (item.todoImpo == 'N' && item.todoComplete=='N') {
                                    test2.add(item)
                                }
                            }

                            for (item in test1) {
                                if (item.todoImpo == 'Y'&& item.todoComplete=='Y') {
                                    test2.add(item)
                                }
                            }

                            for (item in test1) {
                                if (item.todoImpo == 'N'&& item.todoComplete=='Y') {
                                    test2.add(item)
                                }
                            }


                            // 어댑터에 정렬된 리스트 설정
                            todoListAdapter.todoList = test2.toMutableList()
                            todoListAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onFailure(call: Call<List<TodoList>>, t: Throwable) {
                    }
                })  //findAllTodoList
            }
        }

        // 새로운 TodoList 추가 버튼 이벤트 처리
        binding.btnTodoListAdd.setOnClickListener {
            val newTodo = TodoList(0, tId, "")

            // 서버에 새로운 TodoList 추가
            SubClient.retrofit.insertTodoList(tId, newTodo).enqueue(object : Callback<TodoList> {
                override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                    response.body()?.let { item ->
                        todoListAdapter.addTodoList(item)}
                    }
                override fun onFailure(call: Call<TodoList>, t: Throwable) {
                }
            })
        }
    }





    // 기존에 적어놨던 DetailTab1Fragment 내용 - 추후 삭제 예정

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//
//        val binding = FragmentTab1Binding.inflate(inflater, container, false)
//        binding.recyclerView.adapter = TabOneApapter(listOf("test1", "test2", "test3"))
//
//        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
//
//       // return inflater.inflate(R.layout.fragment_tab1, container, false)
//        return binding.root
//    }
}