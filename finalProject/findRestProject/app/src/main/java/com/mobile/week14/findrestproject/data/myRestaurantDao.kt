package com.mobile.week14.findrestproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface myRestaurantDao {
    @Insert
    fun insert(myRestaurant: myRestaurant)

    @Update
    fun update(myRestaurant: myRestaurant)

    @Delete
    fun delete(myRestaurant: myRestaurant)

    @Query("SELECT * FROM restaurantList")
    fun getRestaurants(): List<myRestaurant>

    @Query("SELECT EXISTS (SELECT 1 FROM restaurantList WHERE name = :name LIMIT 1)")
    fun doesRestaurantExist(name: String): Boolean

    @Query("UPDATE restaurantList SET memo = :memo WHERE name = :name")
    fun updateMemo(memo: String?, name: String)

    @Query("DELETE FROM restaurantList WHERE name = :name")
    fun disLikeRestaurant(name: String?)

    @Query("SELECT memo FROM restaurantList WHERE name = :name")
    fun getMemo(name: String?): String?

    @Query("UPDATE restaurantList SET photoPath = :photo WHERE name = :name")
    fun addPhoto(photo: String?, name: String?)

    @Query("SELECT photoPath FROM restaurantList WHERE name = :name")
    fun getPhoto(name: String?): String?
}