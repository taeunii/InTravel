package com.example.intravel.data

import java.io.Serializable

data class TodoList(var tId:Long,
                    var content:String,
                    var tdComplete:Char = 'N',
                    var tdImpo:Char = 'N') : Serializable


// 외래키로 받아오는 t_id 컬럼도 추가해야하나?