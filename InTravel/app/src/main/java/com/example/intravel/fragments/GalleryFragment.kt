package com.example.intravel.fragments

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.R
import com.example.intravel.adapter.GalleryAdapter
import com.example.intravel.client.Client
import com.example.intravel.data.PhotoData
import com.example.intravel.databinding.FragmentGalleryBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.properties.Delegates

class GalleryFragment : Fragment() {

  lateinit var binding: FragmentGalleryBinding
  lateinit var galleryAdapter: GalleryAdapter
  lateinit var filePath: String
  var tId by Delegates.notNull<Long>()

  private val requestCameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
    if (success) {
      photoUri?.let {
        uploadPhoto(it)
      }
    }
  }


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentGalleryBinding.inflate(inflater, container, false)
    return binding.root
  }
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    tId = activity?.intent?.getLongExtra("tId", 0)?: 0

    val photoList = mutableListOf<PhotoData>()

    galleryAdapter = GalleryAdapter(photoList)
    binding.galleryRV.adapter = galleryAdapter
    binding.galleryRV.layoutManager = LinearLayoutManager(requireContext())

//    //    카메라 앱 사용 결과 반환 런처
//    val requestCameraLauncher = registerForActivityResult(
//      ActivityResultContracts.StartActivityForResult()) {
//      val calRatio = calculateInSampleSize(
//        Uri.fromFile(
//          File(filePath)),
//        resources.getDimensionPixelSize(R.dimen.imgSize),
//        resources.getDimensionPixelSize(R.dimen.imgSize)
//      )
//
//      val option = BitmapFactory.Options()
//      option.inSampleSize = calRatio
//      val bitmap = BitmapFactory.decodeFile(filePath, option)
//      bitmap?.let {
////        binding.imgView.setImageBitmap(bitmap)
//      }
//    }

    //    카메라 버튼 클릭
    binding.btnTakePhoto.setOnClickListener {
//      val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//      val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//
////      val storageDir: File? = getExternalFilesDir
////      val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
//
////      filePath = file.absolutePath
//
////      val photoURI: Uri = FileProvider.getUriForFile(it.context, "com.example.intravel.fileprovider", file)
//
////      intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
////      intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//
//      requestCameraLauncher.launch(intent)
      dispatchTakePictureIntent()
    }
  }

//  //  이미지 해상도 조정 펑션
//  private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
//    val options = BitmapFactory.Options()
//    options.inJustDecodeBounds = true
//
////    try {
////      var inputStream = contentResolve
////      BitmapFactory.decodeStream(inputStream, null, options)
////      inputStream!!.close()
////      inputStream = null
////    }
////    catch (e: Exception) {
//////      Toast.makeText(this@GalleryFragment.context, "${e}", Toast.LENGTH_SHORT).show()
////    }
//
//    val (height: Int, width: Int) = options.run {
//      outHeight to outWidth
//    }
//    var inSampleSize = 1
//
//    if (height > reqHeight || width > reqWidth) {
//      val halfHeight: Int = height / 2
//      val halfWidth: Int = width / 2
//
//      while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
//        inSampleSize *= 2
//      }
//    }
//
//    return inSampleSize
//  }

  private fun dispatchTakePictureIntent() {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        photoUri = createImageUri()
        photoUri?.let { uri -> requestCameraLauncher.launch(uri) }
//      takePictureIntent.resolveActivity(requireActivity().packageManager)?.let {
//      }
    }
  }

  private var photoUri: Uri? = null

  private fun createImageUri(): Uri? {
    val contentValues = ContentValues().apply {
      put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
      put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
  }

  private fun uploadPhoto(photoUri: Uri) {
    val photoFile = File(getRealPathFromURI(photoUri))
    val requestFile = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
    val body = MultipartBody.Part.createFormData("photo", photoFile.name, requestFile)

    val call = Client.photoRetrofit.savePhoto(tId, body)
    call.enqueue(object : Callback<PhotoData> {
      override fun onResponse(call: Call<PhotoData>, response: Response<PhotoData>) {
        if (response.isSuccessful) {
//          성공
        }
        else {
//          실패
        }
      }

      override fun onFailure(call: Call<PhotoData>, t: Throwable) {
//        에러
      }

    })
  }

  private fun getRealPathFromURI(contentUri: Uri): String {
    val cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
    cursor?.use {
      it.moveToFirst()
      val index = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
      if (index != -1) {
        return it.getString(index)
      }
    }
    return ""
  }

}



