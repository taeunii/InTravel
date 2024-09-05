package com.example.intravel.adapter

import android.graphics.Typeface
import android.view.KeyEvent
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

        // 상태에 따라 이미지, 텍스트 스타일, 투명도를 업데이트하는 함수
        fun updateUI() {
            // 완료 상태에 따른 이미지 변경
            holder.binding.btnTodoListComplete.setImageResource(
                if (todoItem.tdComplete == 'Y') R.drawable.checked3 else R.drawable.unchecked
            )
            // 중요 여부에 따른 별 아이콘 변경
            holder.binding.btnTodolistImpo.setImageResource(
                if (todoItem.tdImpo == 'Y') R.drawable.star_filled else R.drawable.star_outline
            )
            // 완료 여부에 따른 텍스트 스타일 및 투명도 변경
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
        }

        // 초기 데이터 설정
        holder.binding.tdContent.setText(todoItem.content)
        updateUI()

        // 완료 여부 클릭 이벤트
        holder.binding.btnTodoListComplete.setOnClickListener {
            todoItem.tdComplete = if (todoItem.tdComplete == 'Y') 'N' else 'Y'

            updateUI()
        }

        // 중요도 변경 이벤트
        holder.binding.btnTodolistImpo.setOnClickListener {
            todoItem.tdImpo = if (todoItem.tdImpo == 'Y') 'N' else 'Y'

            updateUI()
        }

        // Enter 키를 눌러 내용 저장 이벤트
        holder.binding.tdContent.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
                todoItem.content = holder.binding.tdContent.text.toString()
                notifyDataSetChanged()
                true
            }
            else {
                false
            }
        }

        // 삭제 버튼 클릭 이벤트
        holder.binding.btnTodolistDelete.setOnClickListener {
            todoList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}