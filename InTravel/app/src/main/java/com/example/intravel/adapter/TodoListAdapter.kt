package com.example.intravel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.R
import com.example.intravel.data.TodoList
import com.example.intravel.databinding.ItemTodolistBinding

class TodoListAdapter(val todoList: MutableList<TodoList>):RecyclerView.Adapter<TodoListAdapter.Holder>() {
    class Holder(val binding: ItemTodolistBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapter.Holder {
        return Holder(ItemTodolistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TodoListAdapter.Holder, position: Int) {
        val todoItem = todoList[position]
        holder.binding.rdoComplete.isChecked = (todoItem.tdComplete == 'Y')
        holder.binding.tdContent.setText(todoItem.content)
        holder.binding.btnImpo.setImageResource(R.drawable.star_outline)
        holder.binding.btnDelete.setImageResource(R.drawable.delete)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}