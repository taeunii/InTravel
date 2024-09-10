package com.example.intravel.data

data class PhotoData(
  val photoId: Long,
  val travId: Long,
  val fileName: String,
  val filePath: String,
  val createDate: String?
)
