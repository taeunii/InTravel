package com.example.intravel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.intravel.client.Client
import com.example.intravel.databinding.ActivityPhotoFullBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoFullActivity : AppCompatActivity() {


  private lateinit var binding: ActivityPhotoFullBinding
  private var photoId: Long = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
//    setContentView(R.layout.activity_photo_full)
    val binding = ActivityPhotoFullBinding.inflate(layoutInflater)
    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }
    val photoUrl = intent.getStringExtra("photoUrl") ?: ""
    photoId = intent.getLongExtra("photoId", 0)

    // Glide를 사용하여 이미지를 로드
    Glide.with(this)
      .load(photoUrl)
      .into(binding.photoDetailImageView)

    // 뒤로 가기 버튼 클릭 시
    binding.btnBack.setOnClickListener {
      finish()  // 현재 액티비티 종료
    }

    // 삭제 버튼 클릭 시
    binding.btnDeletePhoto.setOnClickListener {
      deletePhoto(photoId)
    }
  }

  // 사진 삭제 기능
  private fun deletePhoto(photoId: Long) {
    // 서버에 삭제 요청
    val call = Client.photoRetrofit.deletePhoto(photoId)
    call.enqueue(object : Callback<Void> {
      override fun onResponse(call: Call<Void>, response: Response<Void>) {
        if (response.isSuccessful) {
          // 삭제 성공 시 갤러리로 돌아가기
          val intent = Intent().apply {
            putExtra("deletedPhotoId", photoId)
          }
          setResult(RESULT_OK, intent)
          finish()  // 현재 액티비티 종료
        } else {
          // 에러 처리
          Toast.makeText(this@PhotoFullActivity, "Failed to delete photo", Toast.LENGTH_SHORT).show()
        }
      }

      override fun onFailure(call: Call<Void>, t: Throwable) {
        Toast.makeText(this@PhotoFullActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
      }
    })
  }
}