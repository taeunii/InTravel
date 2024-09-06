package com.example.intravel.client

import com.example.intravel.`interface`.TravelInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
  val retrofit:TravelInterface = Retrofit.Builder()
    .baseUrl("http://10.100.105.2:8811/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(TravelInterface::class.java)
}