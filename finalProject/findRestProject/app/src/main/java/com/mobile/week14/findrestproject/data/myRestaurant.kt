package com.mobile.week14.findrestproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "restaurantList")
data class myRestaurant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String?,
    val category: String?,
    val address: String?,
    val memo: String?,
    val photoPath: String?
):Serializable
