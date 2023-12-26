package ddwu.com.mobile.naverretrofittest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class FileManager(val context: Context) {
    val TAG = "FileManager"

    fun writeText(fileName: String, data: String) {
        context.openFileOutput("test.txt", Context.MODE_PRIVATE).use {
            it.write(data.toByteArray())
        }
    }


    fun readText(fileName: String) : String? {
        val result = StringBuffer()
        context.openFileInput("test.txt").bufferedReader().useLines {
            for (line in it) {
                result.append(line+"\n")
            }
        }
        return result.toString()
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
                    TODO("Not yet implemented")
                }
            })
    }


    fun readInternetImage(url: String, view: ImageView) {
        Glide.with(context)
            .load(url +".jpg")
            .into(view)
    }


    fun readImage(fileName: String, view: ImageView) {
        Glide.with(context)
            .load(context.filesDir.path+"/$fileName")
            .into(view)
    }

    fun removeImage(fileName: String, view: ImageView) {
        context.deleteFile(fileName)
        Glide.with(context)
            .clear(view)
        val deleteFile = File (context.filesDir.path+"/$fileName")
        deleteFile.delete()
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