package com.example.intravel.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intravel.R
import com.example.intravel.adapter.GalleryAdapter
import com.example.intravel.data.PhotoData
import com.example.intravel.databinding.FragmentGalleryBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class GalleryFragment : Fragment() {

  lateinit var binding: FragmentGalleryBinding
  lateinit var galleryAdapter: GalleryAdapter
  lateinit var filePath: String


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

    val tId = activity?.intent?.getLongExtra("tId", 0)?: 0

    val photoList = mutableListOf<PhotoData>()

    galleryAdapter = GalleryAdapter(photoList)
    binding.galleryRV.adapter = galleryAdapter
    binding.galleryRV.layoutManager = LinearLayoutManager(requireContext())

//       런처
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
////                binding.imgView.setImageBitmap(bitmap)
//      }
//    }

//        카메라 버튼 실행
    binding.btnTakePhoto.setOnClickListener {
//      val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//      val storageDir: File? = getExternal
//      val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
//
//      filePath = file.absolutePath
//
//      val photoURI: Uri = FileProvider.getUriForFile(it.context, "com.example.intravel.fileprovider", file)
//
//      val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//      intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//      intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//
//      requestCameraLauncher.launch(intent)
    }
  }

  //    파일 옵션
//  private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
//    val options = BitmapFactory.Options()
//    options.inJustDecodeBounds = true
//
//    try {
//      var inputStream = contentResolver
//      BitmapFactory.decodeStream(inputStream, null, options)
//      inputStream!!.close()
//      inputStream = null
//    }
//    catch (e: Exception) {
//      e.printStackTrace()
//    }
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
//    return inSampleSize
//  }
}



