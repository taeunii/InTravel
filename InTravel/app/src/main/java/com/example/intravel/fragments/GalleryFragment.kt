package com.example.intravel.fragments

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
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
import java.io.File

class GalleryFragment : Fragment() {

  lateinit var binding: FragmentGalleryBinding
  lateinit var galleryAdapter: GalleryAdapter
  lateinit var filePath: String
//  val tId = activity?.intent?.getLongExtra("tId", 0)?: 0
//  var tId by Delegates.notNull<Long>()
  var tId: Long = 0
  var photoId: Long = 0

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
    binding.galleryRV.layoutManager = GridLayoutManager(requireContext(), 3)

    loadPhotoList()

    //    카메라 버튼 클릭
    binding.btnTakePhoto.setOnClickListener {
      dispatchTakePictureIntent()
    }
  }

  private fun dispatchTakePictureIntent() {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        photoUri = createImageUri()
        photoUri?.let { uri -> requestCameraLauncher.launch(uri) }
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
          response.body()?.let { newPhoto ->
            galleryAdapter.photoList.add(newPhoto)
            galleryAdapter.notifyDataSetChanged()
          }
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

  private fun loadPhotoList() {
    val call = Client.photoRetrofit.findPhotoList(tId)
    call.enqueue(object: Callback<List<PhotoData>> {
      override fun onResponse(call: Call<List<PhotoData>>, response: Response<List<PhotoData>>) {
        if (response.isSuccessful) {
          response.body()?.let {
            galleryAdapter.photoList.clear()
            galleryAdapter.photoList.addAll(it)
            galleryAdapter.notifyDataSetChanged()
          }
        }
        else {
//          실패
        }
      }

      override fun onFailure(call: Call<List<PhotoData>>, t: Throwable) {
//        에러
      }
    })
  }

//  override fun onAttach(context: Context) {
//    super.onAttach(context)
//    var intent = Intent(this.context, PhotoFullActivity::class.java)
//
//    intent.putExtra("photoId", photoId)
//
//    startActivity(intent)
//  }
}