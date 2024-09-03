package com.example.intravel.client

import com.example.intravel.`interface`.TempInterface
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object TempClient {
  val retrofit:TempInterface = Retrofit.Builder()
    .baseUrl("http://localhost:8811/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(TempInterface::class.java)
}