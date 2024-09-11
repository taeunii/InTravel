package com.example.intravel.interfaces

import com.example.intravel.data.TravelData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TravelInterface {

    // 완료 여부 전달
    fun updateComplete(@Path("tId") tId:Long,@Body travComplete:Char):Call<TravelData>

    // 완료 여부 조회
    @POST("listComplete")
    fun findComplete(@Body travComplete:Char):Call<List<TravelData>>

    // 전체보기
    @GET("list")
    fun findAll():Call<List<TravelData>>

    // 추가
    @POST("save")
    fun insert(@Body data:TravelData):Call<TravelData>

    // 수정
    @PUT("update/{tId}")
    fun update(@Path("tId") tId:Long,@Body data:TravelData):Call<TravelData>

    // 삭제
    @DELETE("delete/{tId}")
    fun deleteById(@Path("tId") tId:Long):Call<Void>
}