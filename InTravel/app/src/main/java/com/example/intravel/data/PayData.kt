package com.example.intravel.data

data class PayData(var payId:Long,
                   var travId: Long,
                   var moneyId:Long,
                   var payTitle:String?,
                   var plusAmt:Long?,
                   var minusAmt:Long?)
