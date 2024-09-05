package com.example.intravel

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.adapter.TodoListAdapter
import com.example.intravel.data.TodoList
import com.example.intravel.databinding.ActivityMainTodolistBinding

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

        val todoList = mutableListOf<TodoList>()
        val todoListAdapter = TodoListAdapter(todoList)
        binding.todoListRecyclerView.adapter = todoListAdapter
        binding.todoListRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.btnTodoListAdd.setOnClickListener {
            todoListAdapter.todoList.add(TodoList(0, "", 'N', 'N'))
            todoListAdapter.notifyItemInserted(todoListAdapter.todoList.size)
        }

    }
}