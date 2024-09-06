package com.example.intravel.adapter

import android.content.DialogInterface
import android.graphics.Typeface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.intravel.R
import com.example.intravel.client.SubClient
import com.example.intravel.data.TodoList
import com.example.intravel.databinding.ItemTodolistBinding
import retrofit2.Call
import retrofit2.Response

class TodoListAdapter(var todoList: MutableList<TodoList>):RecyclerView.Adapter<TodoListAdapter.TodoHolder>() {

    class TodoHolder(val binding: ItemTodolistBinding):RecyclerView.ViewHolder(binding.root)

    // DB 연결용
//    interface OnItemClickListener {
//        fun onItemClick(todoItem: TodoList, pos: Int)
//    }
//    var onItemClickListener:OnItemClickListener?= null
//
//    // 추가
//    fun addTodoList(todoItem: TodoList) {
//        todoList.add(todoItem)
//        notifyDataSetChanged()
//    }
//
//    // 수정
//    fun updateTodoList(todoItem: TodoList, position: Int) {
//        todoList[position] = todoItem
//        notifyDataSetChanged()
//    }
//
//    // 삭제
//    fun removeTodoList(position: Int) {
//        todoList.removeAt(position)
//        notifyDataSetChanged()
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapter.TodoHolder {
        return TodoHolder(ItemTodolistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TodoListAdapter.TodoHolder, position: Int) {
        val todoItem = todoList[position]

        // 초기 데이터 설정
        holder.binding.tdContent.setText(todoItem.tdContent.toString())

        // UI 업데이트
        updateUI(holder, todoItem)

        // 완료 여부 클릭 이벤트
        holder.binding.btnTodoListComplete.setOnClickListener {
            todoItem.tdComplete = if (todoItem.tdComplete == 'Y') 'N' else 'Y'
//            updateTodoItem(todoItem, position)    //DB 연결용

            updateUI(holder, todoItem)  //DB 연결 전 테스트용 - 추후 삭제
        }

        // 중요도 변경 이벤트
        holder.binding.btnTodolistImpo.setOnClickListener {
            todoItem.tdImpo = if (todoItem.tdImpo == 'Y') 'N' else 'Y'
//            updateTodoItem(todoItem, position)    //DB 연결용

            updateUI(holder, todoItem)  //DB 연결 전 테스트용 - 추후 삭제
        }

        // 삭제 버튼 클릭 이벤트
        holder.binding.btnTodolistDelete.setOnClickListener {
            // DB 연결 전 테스트용 - 추후 삭제
            todoList.removeAt(position)
            notifyDataSetChanged()

            // DB 연결용
//            AlertDialog.Builder(it.context).run {
//                setTitle("삭제하시겠습니까?")
//                setPositiveButton("삭제", object :DialogInterface.OnClickListener {
//                    override fun onClick(p0: DialogInterface?, p1: Int) {
//                        SubClient.retrofit.deleteByIdTodoList(todoItem.tdId).enqueue(object :retrofit2.Callback<Void> {
//                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                                removeTodoList(holder.adapterPosition)
//                            }
//                            override fun onFailure(call: Call<Void>, t: Throwable) {
//                            }
//                        })  //deleteByIdTodoList
//                    }
//                })  //setPositiveButton
//                setNegativeButton("닫기", null)
//                show()
//            }
//            false
        }   //btnTodolistDelete


        // 수정
        // DB 연결용
//        // tdContent 클릭 시 수정 가능
//        holder.binding.tdContent.setOnClickListener {
//            // 내용이 수정 가능하게 focus 및 cursor 활성화
//            holder.binding.tdContent.isEnabled = true
//            holder.binding.tdContent.isFocusableInTouchMode = true
//            holder.binding.tdContent.requestFocus()
//        }
//
//        // 엔터키로 수정 완료
//        holder.binding.tdContent.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                // 수정된 내용을 todoItem에 반영
//                todoItem.tdContent = holder.binding.tdContent.text.toString()
//                updateTodoItem(todoItem, position)
//                // 포커스 해제 및 비활성화
//                holder.binding.tdContent.clearFocus()
//                holder.binding.tdContent.isEnabled = false
//                true
//            }
//            else {
//                false
//            }
//        }

        // DB 연결 전 테스트용 - 추후 삭제
        // Enter 키를 눌러 내용 저장 이벤트
        holder.binding.tdContent.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                todoItem.tdContent = holder.binding.tdContent.text.toString()
                // 포커스 해제 및 비활성화
                holder.binding.tdContent.clearFocus()
                holder.binding.tdContent.isEnabled = false
                // 데이터 변경 알림
                notifyItemChanged(position)
                true
            }
            else {
                false
            }
        }
    }

    // 상태에 따라 이미지, 텍스트 스타일, 투명도를 업데이트하는 함수
    fun updateUI(holder: TodoHolder, todoItem: TodoList) {
        // 완료 상태에 따른 이미지 변경
        holder.binding.btnTodoListComplete.setImageResource(
            if (todoItem.tdComplete == 'Y') R.drawable.checked3 else R.drawable.unchecked
        )
        // 중요 여부에 따른 별 아이콘 변경
        holder.binding.btnTodolistImpo.setImageResource(
            if (todoItem.tdImpo == 'Y') R.drawable.star_filled else R.drawable.star_outline
        )
        // 중요 여부에 따른 텍스트 스타일 및 투명도 변경
        holder.binding.tdContent.setTypeface(
            null,
            if (todoItem.tdImpo == 'Y') Typeface.BOLD else Typeface.NORMAL
        )
        // 완료 여부에 따른 투명도 설정 (완료 상태이면 50% 투명도)
        val alphaValue = if (todoItem.tdComplete == 'Y') 0.5f else 1.0f
        holder.binding.btnTodoListComplete.alpha = alphaValue
        holder.binding.tdContent.alpha = alphaValue
        holder.binding.btnTodolistImpo.alpha = alphaValue
        holder.binding.btnTodolistDelete.alpha = alphaValue

        // 완료 여부에 따라 버튼 비활성화 설정
        val isEnabled = todoItem.tdComplete != 'Y'
        holder.binding.btnTodolistImpo.isEnabled = isEnabled
        holder.binding.btnTodolistDelete.isEnabled = isEnabled
    }

    // DB 연결용
//    fun updateTodoItem(todoItem: TodoList, position: Int) {
//        SubClient.retrofit.updateTodoList(todoItem.tdId, todoItem).enqueue(object :retrofit2.Callback<TodoList> {
//            override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
//                notifyItemChanged(position)
//            }
//            override fun onFailure(call: Call<TodoList>, t: Throwable) {
//            }
//        })  //updateTodoList
//    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}