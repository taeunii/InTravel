package com.example.intravel.`interface`

import com.example.intravel.data.Memo
import com.example.intravel.data.TodoList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SubInterface {

    // todolist
    // 전체보기
    @GET("listAll/{tId}")
    fun findAllTodoList(@Path("tId") travId:Long): Call<List<TodoList>>

    // 추가
    @POST("save/{tId}")
    fun insertTodoList(@Path("tId") travId:Long, @Body todoList: TodoList):Call<TodoList>

    // 수정
    @PUT("update/{tdId}")
    fun updateTodoList(@Path("tdId") tdId:Long, @Body todoList: TodoList):Call<TodoList>

    // 삭제
    @DELETE("delete/{tdId}")
    fun deleteByIdTodoList(@Path("tdId") tdId: Long):Call<Void>


    // memo
    // 전체보기
    @GET("listAll/{tId}")
    fun findAllMemo(@Path("tId") travId:Long): Call<List<Memo>>

    // 추가
    @POST("save/{tId}")
    fun insertMemo(@Path("tId") travId:Long, @Body memo: Memo):Call<Memo>

    // 수정
    @PUT("update/{mId}")
    fun updateMemo(@Path("mtId") mId:Long, @Body memo: Memo):Call<Memo>

    // 삭제
    @DELETE("delete/{mId}")
    fun deleteByIdMemo(@Path("mtId") mId: Long):Call<Void>
}