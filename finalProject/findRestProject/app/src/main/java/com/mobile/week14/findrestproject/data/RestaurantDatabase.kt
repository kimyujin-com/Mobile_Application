package com.mobile.week14.findrestproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobile.week14.findrestproject.ui.RestaurantAdapter

@Database(entities = [myRestaurant::class], version = 1)
abstract class RestaurantDatabase: RoomDatabase() {
    abstract fun myRestaurantDao():myRestaurantDao

    companion object {
        private var instance: RestaurantDatabase? = null

        @Synchronized
        fun getInstance(context: Context): RestaurantDatabase? {
            if (instance == null) {
                synchronized(RestaurantDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RestaurantDatabase::class.java,
                        "restaurant-database"//다른 데이터 베이스랑 이름겹치면 꼬임
                    ).allowMainThreadQueries().build()
                }
            }

            return instance
        }
    }
}