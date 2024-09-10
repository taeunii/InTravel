package com.example.intravel.client

import com.example.intravel.`interface`.TravelInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
  val retrofit:TravelInterface = Retrofit.Builder()
//    .baseUrl("http://10.100.105.220:8811/travel/")
    //.baseUrl("http://192.168.45.150:8811/travel/")
    .baseUrl("http://10.100.105.3:8811/travel/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(TravelInterface::class.java)
}