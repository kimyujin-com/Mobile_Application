package ddwu.com.mobile.cameratest

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import ddwu.com.mobile.cameratest.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.EventListener

class MainActivity : AppCompatActivity() {

    val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val REQUEST_THUMBNAIL_CAPTURE = 1
    val REQUEST_IMAGE_CAPTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        mainBinding.btnThumbnail.setOnClickListener {
            dispatchTakeThumbnailIntent()
        }

        mainBinding.btnOriginal.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }


    private fun dispatchTakeThumbnailIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_THUMBNAIL_CAPTURE)
    }


    private fun dispatchTakePictureIntent() {   // 원본 사진 요청
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = createImageFile()
        if (photoFile != null) {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "ddwu.com.mobile.cameratest.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_THUMBNAIL_CAPTURE -> {
                if (resultCode == RESULT_OK) {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    mainBinding.imageView.setImageBitmap(imageBitmap)
                }
            }
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == RESULT_OK) {
                    setPic()
                }
            }
        }
    }


    lateinit var currentPhotoPath: String   // 현재 이미지 파일의 경로 저장
    var currentPhotoFileName: String? = null  // 현재 이미지 파일명 저장

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val file = File ("${storageDir?.path}/${timeStamp}.jpg")

//        val file = File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        )

        currentPhotoFileName = file.name
        currentPhotoPath = file.absolutePath
        return file
    }


    private fun setPic() {
//        Glide.with(this)
//            .load(File(currentPhotoPath))
//            .into(mainBinding.imageView)

        val targetW: Int = mainBinding.imageView.width
        val targetH: Int = mainBinding.imageView.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
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
            mainBinding.imageView.setImageBitmap(bitmap)
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

}