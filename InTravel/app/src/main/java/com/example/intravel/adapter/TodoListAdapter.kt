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
import com.example.intravel.databinding.ItemTodolistBinding
import retrofit2.Call
import retrofit2.Response

class TodoListAdapter(var todoList: MutableList<TodoList>):RecyclerView.Adapter<TodoListAdapter.TodoHolder>() {

    class TodoHolder(val binding: ItemTodolistBinding):RecyclerView.ViewHolder(binding.root)

//    interface OnItemClickListener{
//        fun onItemClick(todoItem: TodoList, position: Int)
//    }
//    var onItemClickListener:OnItemClickListener?= null

    // 추가
    fun addTodoList(todoItem: TodoList) {
        todoList.add(todoItem)
        notifyDataSetChanged()
//        notifyItemInserted(todoList.size - 1)
    }

    // 수정
    fun updateTodoList(todoItem: TodoList, position: Int) {
        todoList[position] = todoItem
        notifyDataSetChanged()
    }

    // 삭제
    fun removeTodoList(position: Int) {
        todoList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        return TodoHolder(ItemTodolistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val todoItem = todoList[position]

        // 초기 데이터 설정
        holder.binding.tdContent.setText(todoItem.todoContent)

        // 완료 여부 클릭 이벤트
        holder.binding.btnTodoListComplete.setOnClickListener {
            todoItem.todoComplete = if (todoItem.todoComplete == 'Y') 'N' else 'Y'
            updateUI(holder, todoItem)
        }

        // 중요도 변경 이벤트
        holder.binding.btnTodolistImpo.setOnClickListener {
            todoItem.todoImpo = if (todoItem.todoImpo == 'Y') 'N' else 'Y'
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
//            // 수정된 내용을 todoItem에 반영
            holder.binding.tdContent.text.toString()
            holder.binding.btnTodoListComplete.drawable.constantState.toString()
//            holder.binding.btnTodolistImpo.drawable.constantState.toString()
//            todoItem.todoComplete = if (holder.binding.btnTodoListComplete.drawable.constantState == ContextCompat.getDrawable(holder.binding.btnTodoListComplete.context, R.drawable.unchecked)?.constantState) { 'N' }
//            else { 'Y' }
//            todoItem.todoImpo = if (holder.binding.btnTodolistImpo.drawable.constantState == ContextCompat.getDrawable(holder.binding.btnTodolistImpo.context, R.drawable.star_outline)?.constantState) { 'N' }
//            else { 'Y' }

            var todos = TodoList(todoItem.todoId,todoItem.travId,holder.binding.tdContent.text.toString(),'N','N')

            // 서버에 데이터 업데이트 요청
            SubClient.retrofit.updateTodoList(todos.todoId, todos).enqueue(object : retrofit2.Callback<TodoList> {
                override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                    Log.d("todo update","${response.body()}")
                    response.body()?.let { updateItem -> updateTodoList(updateItem, holder.adapterPosition) }

                }
                override fun onFailure(call: Call<TodoList>, t: Throwable) {
                    // 실패 처리
//                    Log.d("")
                }
            })
            holder.binding.tdContent.clearFocus()
            holder.binding.btnTodolistSave.visibility = View.INVISIBLE
        }

    // 삭제 버튼 클릭 이벤트
        holder.binding.btnTodolistDelete.setOnClickListener {
            AlertDialog.Builder(it.context).run {
                setTitle("삭제하시겠습니까?")
                setPositiveButton("삭제") { _, _ ->
                    SubClient.retrofit.deleteByIdTodoList(todoItem.todoId).enqueue(object : retrofit2.Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                removeTodoList(holder.adapterPosition)
                            }
                        }
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            // 실패 처리
                        }
                    })
                }
                setNegativeButton("닫기", null)
                show()
            }
        }











        ////////////////////////

//        // 아이템 뷰 클릭 시 수정 가능하게 설정
//        holder.itemView.setOnClickListener {
//            holder.binding.tdContent.isEnabled = true
//            holder.binding.tdContent.isFocusableInTouchMode = true
//            holder.binding.tdContent.requestFocus()
//        }
//
//        // 완료 여부 클릭 이벤트
//        holder.binding.btnTodoListComplete.setOnClickListener {
//            todoItem.todoComplete = if (todoItem.todoComplete == 'Y') 'N' else 'Y'
//            updateUI(holder, todoItem)
//        }
//
//        // 중요도 변경 이벤트
//        holder.binding.btnTodolistImpo.setOnClickListener {
//            todoItem.todoImpo = if (todoItem.todoImpo == 'Y') 'N' else 'Y'
//            updateUI(holder, todoItem)
//        }
//
//        // 저장 버튼 클릭 이벤트
//        holder.binding.btnTodolistSave.setOnClickListener {
//            // 수정된 내용을 todoItem에 반영
//            todoItem.todoContent = holder.binding.tdContent.text.toString()
//            // 서버에 데이터 업데이트 요청
//            SubClient.retrofit.updateTodoList(todoItem.todoId, todoItem).enqueue(object : retrofit2.Callback<TodoList> {
//                override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
//                    if (response.isSuccessful) {
//                        response.body()?.let {
//                            updateTodoList(it, holder.adapterPosition)
//                            holder.binding.tdContent.clearFocus()
//                            holder.binding.tdContent.isEnabled = false
//                        }
//                    }
//                }
//                override fun onFailure(call: Call<TodoList>, t: Throwable) {
//                    // 실패 처리
//                }
//            })
//        }
//
//        // 삭제 버튼 클릭 이벤트
//        holder.binding.btnTodolistDelete.setOnClickListener {
//            AlertDialog.Builder(it.context).run {
//                setTitle("삭제하시겠습니까?")
//                setPositiveButton("삭제") { _, _ ->
//                    SubClient.retrofit.deleteByIdTodoList(todoItem.todoId).enqueue(object : retrofit2.Callback<Void> {
//                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                            removeTodoList(holder.adapterPosition)
//                        }
//                        override fun onFailure(call: Call<Void>, t: Throwable) {
//                            // 실패 처리
//                        }
//                    })
//                }
//                setNegativeButton("닫기", null)
//                show()
//            }
//        }
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