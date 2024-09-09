package com.example.intravel.data

data class Memo(var memoId:Long,
                var travId:Long,
                var memoTitle:String,
                var memoContent:String,
                var choiceDate:String,  // 선택된 날짜
                var memoCreateDate:String)
