package com.example.intravel.data

data class TodoList(var todoId:Long,
                    var travId:Long,
                    var todoContent:String,
                    var todoComplete:Char ='N',
                    var todoImpo:Char = 'N')
