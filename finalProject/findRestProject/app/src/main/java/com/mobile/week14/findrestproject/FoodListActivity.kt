package com.mobile.week14.findrestproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.week14.findrestproject.data.RestaurantDatabase
import com.mobile.week14.findrestproject.databinding.ActivityFoodlistBinding
import com.mobile.week14.findrestproject.ui.FoodListAdapter

class FoodListActivity: AppCompatActivity() {
    val foodlistBinding by lazy {
        ActivityFoodlistBinding.inflate(layoutInflater)
    }
    val adapter by lazy {
        FoodListAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(foodlistBinding.root)

        val restaurantDB = RestaurantDatabase.getInstance(this)!!

        foodlistBinding.rvFoodList.adapter = adapter
        foodlistBinding.rvFoodList.layoutManager = LinearLayoutManager(this)

        adapter.foodList = restaurantDB.myRestaurantDao().getRestaurants()
        Log.d("TAG4", adapter.foodList.toString())

        adapter.setOnItemLongClickListener(object : FoodListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int) {
                val restaurant = adapter.foodList?.get(position)
                restaurantDB.myRestaurantDao().disLikeRestaurant(restaurant?.name)
                adapter.foodList = restaurantDB.myRestaurantDao().getRestaurants()
                adapter.notifyDataSetChanged()
            }
        })

        adapter.setOnItemClickListener(object : FoodListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val restaurant = adapter.foodList?.get(position)
                val Sharing_intent = Intent(Intent.ACTION_SEND)
                Sharing_intent.type = "text/plain"

                val Test_Message = restaurant?.name.toString()

                Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message)

                val Sharing = Intent.createChooser(Sharing_intent, "공유하기")
                startActivity(Sharing)
            }
        })
    }
}