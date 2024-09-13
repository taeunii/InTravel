package com.example.intravel.data

data class PhotoData(
    val photoId: Long,
    val travId: Long,
    val fileName: String,
    val filePath: String,
    // 추가
    val latitude: String,
    val longitude: String,

    val createDate: String?
)