package com.example.intravel.`interface`

import com.example.intravel.data.Memo
import com.example.intravel.data.TodoList
import retrofit2.Call

interface SubInterface {

    // todolist
    // 전체보기
    fun findAllTodoList(): Call<List<TodoList>>

    // 추가
    fun insertTodoList(todoList: TodoList):Call<TodoList>

    // 수정
    fun updateTodoList(tdId:Long, todoList: TodoList):Call<TodoList>

    // 삭제
    fun deleteByIdTodoList(tdId: Long):Call<Void>


    // memo
    // 전체보기
    fun findAllMemo(): Call<List<Memo>>

    // 추가
    fun insertMemo(memo: Memo):Call<Memo>

    // 수정
    fun updateMemo(mId:Long, memo: Memo):Call<Memo>

    // 삭제
    fun deleteByIdMemo(mId: Long):Call<Void>
}