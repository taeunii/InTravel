package com.example.intravel.`interface`

import com.example.intravel.data.Memo
import com.example.intravel.data.MoneyData
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
    @GET("todo/listAll/{tId}")
    fun findAllTodoList(@Path("tId") travId:Long): Call<List<TodoList>>

    // 추가
    @POST("todo/save/{tId}")
    fun insertTodoList(@Path("tId") travId:Long, @Body todoList: TodoList):Call<TodoList>

    // 수정
    @PUT("todo/update/{tdId}")
    fun updateTodoList(@Path("tdId") tdId:Long, @Body todoList: TodoList):Call<TodoList>

    // 삭제
    @DELETE("todo/delete/{tdId}")
    fun deleteByIdTodoList(@Path("tdId") tdId: Long):Call<Void>


    // memo
    // 전체보기
    @GET("memo/listAll/{tId}")
    fun findAllMemo(@Path("tId") travId:Long): Call<List<Memo>>

    // 추가
    @POST("memo/save/{tId}")
    fun insertMemo(@Path("tId") travId:Long, @Body memo: Memo):Call<Memo>

    // 수정
    @PUT("memo/update/{mId}")
    fun updateMemo(@Path("mId") mId:Long, @Body memo: Memo):Call<Memo>

    // 삭제
    @DELETE("memo/delete/{mId}")
    fun deleteByIdMemo(@Path("mId") mId: Long):Call<Void>


    // Money
    // 전체보기
    // 추가
    fun insertMoney(@Path("tId") travId: Long, @Body money:MoneyData):Call<MoneyData>
    // 수정
    // 삭제
}