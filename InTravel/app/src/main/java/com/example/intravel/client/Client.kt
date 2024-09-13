package com.example.intravel.client

import com.example.intravel.`interface`.TravelInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
  val retrofit:TravelInterface = Retrofit.Builder()
//    .baseUrl("http://10.100.105.216:8811/travel/")
    .baseUrl("http://192.168.219.108:8811/travel/")   // 추후 삭제예정
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(TravelInterface::class.java)
}