package ddwu.com.mobile.naverretrofittest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ddwu.com.mobile.naverretrofittest.databinding.ActivityDetailBinding
import java.io.File
import java.io.FileOutputStream

class DetailActivity : AppCompatActivity() {
    val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    val fileManager: FileManager by lazy {
        FileManager(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)

        val url = intent.getStringExtra("url") as String
        val registerTime = fileManager.getCurrentTime()

        detailBinding.btnSave.setOnClickListener {
            fileManager.writeImage(registerTime+".jpg", url)
        }

        detailBinding.btnRead.setOnClickListener {
            fileManager.readImage(registerTime+".jpg", detailBinding.imgBookCover)
        }

        detailBinding.btnInit.setOnClickListener {
            detailBinding.imgBookCover.setImageResource(R.mipmap.ic_launcher)
        }

        detailBinding.btnRemove.setOnClickListener {
            fileManager.removeImage(registerTime+".jpg", detailBinding.imgBookCover)
        }
    }
}