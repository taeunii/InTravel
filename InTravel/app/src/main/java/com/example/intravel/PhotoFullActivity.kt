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
    binding = ActivityPhotoFullBinding.inflate(layoutInflater)
    setContentView(binding.root)

//    ViewCompat 쓰니까 오류남
//    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//      insets
//    }

    val fileName = intent.getStringExtra("fileName") ?: ""
    val url = intent.getStringExtra("url") ?: ""
    photoId = intent.getLongExtra("photoId", 0)

    Glide.with(this)
      .load(url + fileName)
      .into(binding.photoDetailImageView)

    binding.btnBack.setOnClickListener {
      finish()
    }
  }
}