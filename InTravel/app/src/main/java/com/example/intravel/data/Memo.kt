package com.example.intravel.data

data class Memo(var mId:Long,
                var mTitle:String,
                var mContent:String)

// 외래키로 받아오는 t_id 컬럼도 추가해야하나?