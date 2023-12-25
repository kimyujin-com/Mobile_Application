package com.mobile.week14.findrestproject.data

import com.google.gson.annotations.SerializedName

data class RestaurantRoot(
    val items: List<Item>
)

data class Item(
    val title: String,
    val link: String?,
    val category: String,
    val description: String,
    val telephone: String?,
    val address: String,
    val roadAddress: String,
    val mapx: Long,
    val mapy: Long
)