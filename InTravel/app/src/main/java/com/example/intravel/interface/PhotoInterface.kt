package com.example.intravel.`interface`

import com.example.intravel.data.PhotoData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface PhotoInterface {

  @Multipart
  @GET("/photo/list/{travId}")
  fun findPhotoList(@Path("travId") travId:Long): Call<List<PhotoData>>

  @Multipart
  @POST("/photo/save/{travId}")
  fun savePhoto(@Path("travId") travId: Long, @Part photo: MultipartBody.Part): Call<PhotoData>

  @PUT("/photo/update/{photoId}")
  fun updatePhoto(@Path("photoId") photoId: Long, @Body photoData: PhotoData): Call<PhotoData>

  @DELETE("/photo/delete/{photoId}")
  fun deletePhoto(@Path("photoId") photoId: Long): Call<Void>

}