package com.example.intravel.client

import com.example.intravel.`interface`.SubInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SubClient {
    val retrofit:SubInterface = Retrofit.Builder()
        .baseUrl("http://192.168.219.106:8811/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SubInterface::class.java)
}