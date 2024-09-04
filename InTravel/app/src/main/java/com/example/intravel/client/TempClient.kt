package com.example.intravel.client

import com.example.intravel.`interface`.TempInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TempClient {
    val retrofit:TempInterface = Retrofit.Builder()
        .baseUrl("http://10.100.105.216:8811")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TempInterface::class.java)
}