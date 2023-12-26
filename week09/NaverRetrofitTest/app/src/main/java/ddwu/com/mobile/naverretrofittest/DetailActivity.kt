package ddwu.com.mobile.naverretrofittest

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ddwu.com.mobile.naverretrofittest.data.FileManager
import ddwu.com.mobile.naverretrofittest.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    var imageUrl : String? = null
    val fileManager by lazy {
        FileManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)

        imageUrl = intent.getStringExtra("url")

        Glide.with(applicationContext)
            .load(imageUrl)
            .into(detailBinding.imgBookCover)

        detailBinding.btnSave.setOnClickListener {
            fileManager.writeImage("image.jpg", imageUrl!!)
        }

        detailBinding.btnRead.setOnClickListener {
            fileManager.readImage("image.jpg", detailBinding.imgBookCover)
        }

        detailBinding.btnInit.setOnClickListener {
            detailBinding.imgBookCover.setImageResource(R.mipmap.ic_launcher)
        }
   
        detailBinding.btnRemove.setOnClickListener {
            fileManager.deleteImageFile("image.jpg")
        }
    }
}