package com.example.intravel.data

data class TodoList(var tdId:Long,
                    var tId:Long,
                    var tdContent:String,
                    var tdComplete:Char,
                    var tdImpo:Char)

// toComplete(완료), tdImpo(중요) 기본값 N