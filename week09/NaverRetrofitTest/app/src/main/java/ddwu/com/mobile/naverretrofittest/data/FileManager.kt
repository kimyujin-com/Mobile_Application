package ddwu.com.mobile.naverretrofittest.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.text.SimpleDateFormat
import java.util.Date

class FileManager(val context: Context) {
    val TAG = "FileManager"

    fun writeText(fileName: String, data: String) {

    }


    fun readText(fileName: String) : String? {

        return null
    }

    fun writeImage(fileName: String, imageUrl: String) {
        Glide.with(context)
            .asBitmap()

            .load(imageUrl)
            .into( object: CustomTarget<Bitmap> (350, 350) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.d(TAG, "Loading is canceled!")
                }
            } )
    }


    fun readInternetImage(url: String, view: ImageView) {
//        Glide.with(context)
//            .load(url)
//            .into(view)

//        Glide.with(context)
//            .asBitmap()
//            .load(url)
//            .into(
//
//            )
    }


    fun readImage(fileName: String, view: ImageView) {
        Glide.with(context)
            .load(context.filesDir.path + "/image.jpg")
            // 동일한 이름의 이미지를 읽어올 경우 cache 에 저장한 것을 읽어오게 되므로
            // cache 사용을 제한하여 새로 읽게 지정
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(view)
    }

    fun deleteImageFile(fileName: String) {
        context.deleteFile("image.jpg")
    }



    // Checks if a volume containing external storage is available
    // for read and write.
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // Checks if a volume containing external storage is available to at least read.
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    fun getImageFileName(path: String) : String {
        val fileName = path.slice(IntRange( path.lastIndexOf("/")+1, path.length-1))
        return fileName
    }

    fun getCurrentTime() : String {
        return SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    }
}