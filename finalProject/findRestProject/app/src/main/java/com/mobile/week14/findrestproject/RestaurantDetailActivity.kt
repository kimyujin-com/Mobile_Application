package com.mobile.week14.findrestproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.mobile.week14.findrestproject.data.RestaurantDatabase
import com.mobile.week14.findrestproject.data.myRestaurant
import com.mobile.week14.findrestproject.databinding.ActivityRestaurantDetailBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class RestaurantDetailActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityRestaurantDetailBinding.inflate(layoutInflater)
    }
    val restaurantDB = RestaurantDatabase.getInstance(this)!!
    val REQUEST_THUMBNAIL_CAPTURE = 1
    val REQUEST_IMAGE_CAPTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val restaurant = myRestaurant(
            0,
            intent.getStringExtra("title").toString(),
            intent.getStringExtra("category").toString(),
            intent.getStringExtra("address").toString(),
            null, null
        )
        setViews(restaurant)
        binding.itemRestaurantHeartIv.setOnClickListener{
            if (isLiked(restaurant.name as String)) {
                binding.itemRestaurantHeartIv.setImageResource(R.drawable.heart_icon)
                restaurantDB.myRestaurantDao().disLikeRestaurant(restaurant.name)
                binding.memoButton.isEnabled = false
                binding.btnOriginal.isEnabled = false
            }
            else {
                binding.itemRestaurantHeartIv.setImageResource(R.drawable.heart_like_love_icon)
                restaurantDB.myRestaurantDao().insert(restaurant)
                binding.memoButton.isEnabled = true
                binding.btnOriginal.isEnabled = true
            }
        }
        binding.memoButton.setOnClickListener{
            restaurantDB.myRestaurantDao().updateMemo(binding.editMemo.text.toString(), restaurant.name as String)
        }

        binding.btnOriginal.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun setViews(restaurant: myRestaurant) {
        binding.itemRestaurantTitleTv.text = restaurant.name
        binding.itemRestaurantCategoryTv.text = restaurant.category
        binding.itemRestaurantAddressTv.text = restaurant.address
        val memoString: String? = restaurantDB.myRestaurantDao().getMemo(restaurant.name)
        val memoEditable: Editable = if (memoString != null) {
            Editable.Factory.getInstance().newEditable(memoString)
        } else {
            Editable.Factory.getInstance().newEditable("") // or handle it in a way that makes sense for your app
        }
        binding.editMemo.text = memoEditable

        if(isLiked(restaurant.name as String)) {
            binding.itemRestaurantHeartIv.setImageResource(R.drawable.heart_like_love_icon)
            binding.memoButton.isEnabled = true
            binding.btnOriginal.isEnabled = true
        } else {
            binding.itemRestaurantHeartIv.setImageResource(R.drawable.heart_icon)
            binding.memoButton.isEnabled = false
            binding.btnOriginal.isEnabled = false
        }
    }

    private fun isLiked(restaurantName: String): Boolean {
        val isLiked: Boolean = restaurantDB.myRestaurantDao().doesRestaurantExist(restaurantName)
        return isLiked
    }

    lateinit var currentPhotoPath: String   // 현재 이미지 파일의 경로 저장
    var currentPhotoFileName: String? = null  // 현재 이미지 파일명 저장

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val file = File ("${storageDir?.path}/${timeStamp}.jpg")

        currentPhotoFileName = file.name
        currentPhotoPath = file.absolutePath

        restaurantDB.myRestaurantDao().addPhoto(currentPhotoPath, intent.getStringExtra("title").toString())
        return file
    }

    private fun setPic() {
        val targetW: Int = binding.imageView.width
        val targetH: Int = binding.imageView.height

        val bmOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(currentPhotoPath, this)

            val photoW: Int = outWidth
            val photoH: Int = outHeight


            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            binding.imageView.setImageBitmap(bitmap)
        }
    }

    private fun dispatchTakePictureIntent() {   // 원본 사진 요청
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = createImageFile()
        if (photoFile != null) {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.mobile.week14.findrestproject.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == RESULT_OK) {
                    setPic()
                }
            }
        }
    }
}