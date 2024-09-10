package com.example.intravel.client

import com.example.intravel.`interface`.SubInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SubClient {
    val retrofit:SubInterface = Retrofit.Builder()
//        .baseUrl("http://10.100.105.216:8811/")
        .baseUrl("http:/192.168.219.105:8811/")   // 추후 삭제예정
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SubInterface::class.java)
}