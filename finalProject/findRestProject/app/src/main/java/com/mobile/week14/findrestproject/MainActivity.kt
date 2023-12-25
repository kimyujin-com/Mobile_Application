package com.mobile.week14.findrestproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.week14.findrestproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        mainBinding.memoButton.setOnClickListener() {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }
        mainBinding.memoButton2.setOnClickListener() {
            val intent = Intent(this@MainActivity, FoodListActivity::class.java)
            startActivity(intent)
        }
    }
}