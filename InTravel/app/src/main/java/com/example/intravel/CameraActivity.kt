package com.example.intravel

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.intravel.databinding.ActivityProviderBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class CameraActivity : AppCompatActivity() {

  lateinit var binding: ActivityProviderBinding
  lateinit var filePath: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    binding = ActivityProviderBinding.inflate(layoutInflater)
    setContentView(binding.root)
//        setContentView(R.layout.activity_provider)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }


//    카메라 앱 사용 결과 반환 런처
    val requestCameraLauncher = registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()) {
        val calRatio = calculateInSampleSize(
          Uri.fromFile(
            File(filePath)),
            resources.getDimensionPixelSize(R.dimen.imgSize),
            resources.getDimensionPixelSize(R.dimen.imgSize)
        )

        val option = BitmapFactory.Options()
        option.inSampleSize = calRatio
        val bitmap = BitmapFactory.decodeFile(filePath, option)
        bitmap?.let {
          binding.imgView.setImageBitmap(bitmap)
        }
      }

//    카메라 버튼 클릭
    binding.btnCamera.setOnClickListener {
      val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
      val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
      val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)

      filePath = file.absolutePath

      val photoURI: Uri = FileProvider.getUriForFile(this, "com.example.intravel.fileprovider", file)

      val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
      intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
      intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

      requestCameraLauncher.launch(intent)
    }
  }

//  이미지 해상도 조정 펑션
  private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true

    try {
      var inputStream = contentResolver.openInputStream(fileUri)
      BitmapFactory.decodeStream(inputStream, null, options)
      inputStream!!.close()
      inputStream = null
    }
    catch (e: Exception) {
      e.printStackTrace()
    }

    val (height: Int, width: Int) = options.run {
      outHeight to outWidth
    }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
      val halfHeight: Int = height / 2
      val halfWidth: Int = width / 2

      while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
        inSampleSize *= 2
      }
    }

    return inSampleSize
  }
}