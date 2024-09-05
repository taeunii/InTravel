package com.example.intravel.`interface`

import com.example.intravel.data.MainData
import retrofit2.Call

interface MainInterface {

    // 완료여부 조회
    fun findComplete(tCompleted:Char):Call<List<MainData>>

    ////////////////// 공통 함수 ///////////////////
    // 전체보기
    fun findAll():Call<List<MainData>>

    // 추가
    fun insert(data:MainData):Call<MainData>

    // 수정
    fun update(id:Long,data:MainData):Call<MainData>

    // 삭제
    fun deleteById(id:Long):Call<Void>
}