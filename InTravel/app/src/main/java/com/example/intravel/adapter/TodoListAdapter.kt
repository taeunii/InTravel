package com.example.intravel.adapter

import android.content.DialogInterface
import android.graphics.Typeface
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.MainActivity
import com.example.intravel.R
import com.example.intravel.client.SubClient
import com.example.intravel.data.TodoList
import com.example.intravel.data.TravelData
import com.example.intravel.databinding.ItemTodolistBinding
import retrofit2.Call
import retrofit2.Response
import java.util.Collections

class TodoListAdapter(var todoList: MutableList<TodoList>):RecyclerView.Adapter<TodoListAdapter.TodoHolder>() {

    inner class TodoHolder(val binding: ItemTodolistBinding):RecyclerView.ViewHolder(binding.root){

        init{

        }


    }

    // 추가
    fun addTodoList(todoItem: TodoList) {
        onItemClickListener!!.onItemClick()
        todoList.add(todoItem)
        notifyDataSetChanged()
    }

    // 수정
    fun updateTodoList(todoItem: TodoList, position: Int) {
//        // 기존 항목을 제거
//        todoList.removeAt(position)
//
//        // 완료 여부에 따라 새 위치 결정
//        val newPosition = if (todoItem.todoComplete == 'Y') {
//            todoList.size  // 완료된 항목을 리스트의 맨 아래로 이동
//        }
//        else {
//            // 완료되지 않은 항목을 `todoId` 순서에 맞게 재배치
//            todoList.indexOfFirst { it.todoId > todoItem.todoId }.takeIf { it >= 0 } ?: todoList.size
//        }

        // 새 위치에 항목 추가
//        todoList.add(newPosition, todoItem)

//        var posZeroItem = todoList.get(0)

//        todoList[position] = todoItem
        if(todoItem.todoImpo == 'Y'){
//            todoList.add(0,todoItem)
//            todoList.removeAt(position+1)
            Collections.swap(todoList,position,0)
            notifyItemMoved(position, 0)
        }
        else{
            todoList[position] = todoItem
            notifyDataSetChanged()
        }
          // RecyclerView에 변경 사항 알림
    }

    // 삭제
    fun removeTodoList(position: Int) {
        todoList.removeAt(position)
        notifyDataSetChanged()
    }


    interface OnItemClickListener{
        fun onItemClick()
    }
    var onItemClickListener:OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        return TodoHolder(ItemTodolistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val todoItem = todoList[position]

        // 초기 데이터 설정
        holder.binding.tdContent.setText(todoItem.todoContent)
        if (todoItem.todoComplete == 'Y') {
            holder.binding.btnTodoListComplete.setImageResource(R.drawable.checked3)
        } else {
            holder.binding.btnTodoListComplete.setImageResource(R.drawable.unchecked)
        }
        if (todoItem.todoImpo == 'Y') {
            holder.binding.btnTodolistImpo.setImageResource(R.drawable.star_filled)
        } else {
            holder.binding.btnTodolistImpo.setImageResource(R.drawable.star_outline)
        }
        updateUI(holder, todoItem)

        // 완료 여부 클릭 이벤트
        holder.binding.btnTodoListComplete.setOnClickListener {
            val isCompleted = todoItem.todoComplete == 'Y'

            if (isCompleted) {
                holder.binding.btnTodoListComplete.setImageResource(R.drawable.unchecked)
                todoItem.todoComplete = 'N'
            }
            else {
                holder.binding.btnTodoListComplete.setImageResource(R.drawable.checked3)
                todoItem.todoComplete = 'Y'
            }

            var todos = TodoList(todoItem.todoId, todoItem.travId, holder.binding.tdContent.text.toString(), todoItem.todoComplete, todoItem.todoImpo)

            // 서버에 데이터 업데이트 요청
            SubClient.retrofit.updateTodoList(todos.todoId, todos).enqueue(object : retrofit2.Callback<TodoList> {
                override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                    response.body()?.let { updateItem -> updateTodoList(updateItem, holder.adapterPosition) }
                    onItemClickListener!!.onItemClick()
                }
                override fun onFailure(call: Call<TodoList>, t: Throwable) {
                }
            })
            updateUI(holder, todoItem)
            holder.binding.btnTodolistSave.visibility = View.INVISIBLE
        }

        // 중요도 변경 이벤트
        holder.binding.btnTodolistImpo.setOnClickListener {
            val isImpo = todoItem.todoImpo == 'Y'

            if (isImpo) {
                holder.binding.btnTodolistImpo.setImageResource(R.drawable.star_outline)
                todoItem.todoImpo = 'N'
            }
            else {
                holder.binding.btnTodolistImpo.setImageResource(R.drawable.star_filled)
                todoItem.todoImpo = 'Y'
            }
            updateUI(holder, todoItem)
            holder.binding.btnTodolistSave.visibility = View.VISIBLE
        }

        // 텍스트 필드 포커스 이벤트
        holder.binding.tdContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.binding.btnTodolistSave.visibility = View.VISIBLE
            }
        }

        // 수정(저장) 버튼 클릭 이벤트
        holder.binding.btnTodolistSave.setOnClickListener {

            var todos = TodoList(todoItem.todoId, todoItem.travId, holder.binding.tdContent.text.toString(), todoItem.todoComplete, todoItem.todoImpo)
            // 서버에 데이터 업데이트 요청
            SubClient.retrofit.updateTodoList(todos.todoId, todos).enqueue(object : retrofit2.Callback<TodoList> {
                override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
//                    Log.d("todo update","${response.body()}")
                    response.body()?.let { updateItem ->
                        updateTodoList(updateItem, holder.adapterPosition) }


                }
                override fun onFailure(call: Call<TodoList>, t: Throwable) {
                }
            })
            holder.binding.tdContent.clearFocus()
            holder.binding.btnTodolistSave.visibility = View.INVISIBLE
        }

    // 삭제 버튼 클릭 이벤트
        holder.binding.btnTodolistDelete.setOnClickListener {
            AlertDialog.Builder(it.context).run {
                setTitle("삭제하시겠습니까?")
                setPositiveButton("삭제", object :DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        SubClient.retrofit.deleteByIdTodoList(todoItem.todoId).enqueue(object :retrofit2.Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                removeTodoList(holder.adapterPosition)
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                            }
                        })
                    }
                })
                setNegativeButton("닫기", null)
                show()
            }
        }
    }

    // 상태에 따라 이미지, 텍스트 스타일, 투명도를 업데이트하는 함수
    fun updateUI(holder: TodoHolder, todoItem: TodoList) {
        // 완료 상태에 따른 이미지 변경
        holder.binding.btnTodoListComplete.setImageResource(
            if (todoItem.todoComplete == 'Y') R.drawable.checked3 else R.drawable.unchecked
        )

        // 완료 여부에 따른 투명도 설정 (완료 상태이면 50% 투명도)
        val alphaValue = if (todoItem.todoComplete == 'Y') 0.5f else 1.0f
        holder.binding.root.alpha = alphaValue

        // 완료 여부에 따라 버튼 비활성화 설정
        val isEnabled = todoItem.todoComplete != 'Y'
        holder.binding.tdContent.isEnabled = isEnabled
        holder.binding.btnTodolistImpo.isEnabled = isEnabled
        holder.binding.btnTodolistDelete.isEnabled = isEnabled
        holder.binding.btnTodolistSave.isEnabled = isEnabled

        // 중요 여부에 따른 별 아이콘 변경
        holder.binding.btnTodolistImpo.setImageResource(
            if (todoItem.todoImpo == 'Y') R.drawable.star_filled else R.drawable.star_outline
        )

        // 중요 여부에 따른 텍스트 스타일 변경
        holder.binding.tdContent.setTypeface(
            null,
            if (todoItem.todoImpo == 'Y') Typeface.BOLD else Typeface.NORMAL
        )
    }


    override fun getItemCount(): Int {
        return todoList.size
    }
}