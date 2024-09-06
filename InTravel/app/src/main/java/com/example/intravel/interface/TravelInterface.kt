package com.example.intravel.`interface`

import com.example.intravel.data.TravelData
import retrofit2.Call

interface TravelInterface {

    // 완료여부 조회
    fun findComplete(tCompleted:Char):Call<List<TravelData>>

    ////////////////// 공통 함수 ///////////////////
    // 전체보기
    fun findAll():Call<List<TravelData>>

    // 추가
    fun insert(data:TravelData):Call<TravelData>

    // 수정
    fun update(id:Long,data:TravelData):Call<TravelData>

    // 삭제
    fun deleteById(id:Long):Call<Void>
}