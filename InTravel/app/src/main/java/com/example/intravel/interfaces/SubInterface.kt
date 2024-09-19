package com.example.intravel.`interface`

import com.example.intravel.data.Maps
import com.example.intravel.data.Memo
import com.example.intravel.data.MoneyData
import com.example.intravel.data.PayData
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
    fun deleteByIdTodoList(@Path("tdId") tdId:Long):Call<Void>


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
    @GET("money/list/{tId}")
    fun findAllMoneyList(@Path("tId")travId: Long):Call<List<MoneyData>>

    // 추가
    @POST("money/save/{tId}")
    fun insertMoney(@Path("tId") travId: Long, @Body money:MoneyData):Call<MoneyData>

    // 수정
    @PUT("money/update/{mId}")
    fun updateMoney(@Path("mId") moneyId: Long, @Body money: MoneyData):Call<MoneyData>

    // 삭제
    @DELETE("money/delete/{mId}")
    fun deleteByIdMoney(@Path("mId") moneyId: Long):Call<Void>


    // Pay
    // 전체보기
    @GET("pay/moneyList/{mId}")
    fun findAllPayList(@Path("mId")moneyId: Long):Call<List<PayData>>

    @GET("pay/travList/{tId}")
    fun findTravIdPayList(@Path("tId") travId: Long):Call<List<PayData>>

    // 추가
    @POST("pay/save/{mId}")
    fun insertPay(@Path("mId") moneyId: Long, @Body pay:PayData):Call<PayData>

    // 수정
    @PUT("pay/update/{pId}")
    fun updatePay(@Path("pId") payId: Long, @Body pay: PayData):Call<PayData>

    // 삭제
    @DELETE("pay/delete/{pId}")
    fun deleteByIdPay(@Path("pId") payId: Long):Call<Void>

  // maps (map 이라고 하면 기존에 설정된 map이랑 충돌생김)
  // 전체보기
  @GET("map/list/{tId}")
  fun findAllMap(@Path("tId") travId:Long): Call<List<Maps>>

  // 추가
  @POST("map/save/{tId}")
  fun insertMap(@Path("tId") travId:Long, @Body map: Maps):Call<Maps>

  // 수정
  @PUT("map/update/{mId}")
  fun updateMap(@Path("mId") mId:Long, @Body map: Maps):Call<Maps>

  // 삭제
  @DELETE("map/delete/{mId}")
  fun deleteByIdMap(@Path("mId") mId: Long):Call<Void>
}