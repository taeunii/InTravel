package com.example.intravel.data

data class TravelData(var travId:Long,
                      var travTitle: String,
                      var createDate:String?,
                      var startDate:String,
                      var endDate:String,
                      var cate:String?,
                      var travComplete:Char?)

// 생성일은 디비에서 오늘날짜로 자동으로 생성
// 완료여부 N 진행중 Y 완료됨 / 기본값 N