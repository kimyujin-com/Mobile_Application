package ddwu.com.mobile.ballviewtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ddwu.com.mobile.ballviewtest.databinding.ActivityMainBinding
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)
    }

    override fun onResume() {
        super.onResume()
        mainBinding.ballView.startListening()
    }

    override fun onPause() {
        super.onPause()
        mainBinding.ballView.stopListening()
    }
}