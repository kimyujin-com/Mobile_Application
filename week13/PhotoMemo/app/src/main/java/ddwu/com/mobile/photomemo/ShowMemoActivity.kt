package ddwu.com.mobile.photomemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import ddwu.com.mobile.photomemo.data.MemoDto
import ddwu.com.mobile.photomemo.databinding.ActivityShowMemoBinding
import java.io.File

class ShowMemoActivity : AppCompatActivity() {

    val TAG = "ShowMemoActivityTag"

    val showMemoBinding by lazy {
        ActivityShowMemoBinding.inflate(layoutInflater)
    }

    lateinit var memoDto: MemoDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(showMemoBinding.root)

        showMemoBinding.btnModify.setOnClickListener {
            Toast.makeText(this, "Implement modifying data", Toast.LENGTH_SHORT).show()
        }

        showMemoBinding.btnClose.setOnClickListener {
            finish()
        }

        /*전달받은 intent 에서 memoDto 를 읽어온 후 각 view 에 지정*/

        // Intent에서 MemoDto를 읽어옴
        memoDto = intent.getSerializableExtra("memoDto") as MemoDto

        // MemoDto의 데이터를 뷰에 설정
        showMemoBinding.tvMemo.text = toEditable(memoDto.memo)


        // 이미지 파일이 있다면 Glide를 사용하여 이미지를 표시
        if (memoDto.photoName != null) {
            val imageFile =
                File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), memoDto.photoName!!)
            Glide.with(this)
                .load(imageFile)
                .into(showMemoBinding.ivPhoto)
        }
    }

    // String을 Editable로 변환하는 함수
    private fun toEditable(text: String?): Editable {
        return SpannableStringBuilder(text)
    }
}