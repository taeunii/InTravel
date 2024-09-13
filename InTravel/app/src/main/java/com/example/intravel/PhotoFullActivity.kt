package com.example.intravel

import android.content.ContentValues
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.intravel.client.Client
import com.example.intravel.databinding.ActivityPhotoFullBinding
import retrofit2.Call
import retrofit2.Response
import java.io.OutputStream

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
//    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
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

    binding.btnSavePhotoToGallery.setOnClickListener {
      AlertDialog.Builder(it.context).run {
        setTitle("서버에 저장된 사진을 갤러리로 다운로드 하시겠습니까?")
        setPositiveButton("다운로드", object : DialogInterface.OnClickListener {
          override fun onClick(p0: DialogInterface?, p1: Int) {
            savePhotoToGallery(url+fileName)
          }
        })
        setNegativeButton("취소", null)
        show()
      }
    }
  }

  private fun savePhotoToGallery(filePathAll:String) {
    Glide.with(this)
      .asBitmap()
      .load(filePathAll)
      .into(object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
          val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
          }

          val resolver = contentResolver
          val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

          uri?.let {
            var outputStream: OutputStream? = null
            try {
              outputStream = resolver.openOutputStream(it)
              if (outputStream != null) {
                resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
              }
              outputStream?.flush()
              Toast.makeText(this@PhotoFullActivity, "사진이 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show()
            }
            catch (e: Exception) {
              e.printStackTrace()
              Toast.makeText(this@PhotoFullActivity, "사진 저장 실패", Toast.LENGTH_SHORT).show()
            }
            finally {
              outputStream?.close()
            }
          }
        }
        override fun onLoadCleared(placeholder: Drawable?) {

        }
      })
  }
}