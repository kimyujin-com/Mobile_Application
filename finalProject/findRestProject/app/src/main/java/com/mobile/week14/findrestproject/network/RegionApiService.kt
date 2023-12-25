package com.mobile.week14.findrestproject.network

import com.mobile.week14.findrestproject.data.RestaurantRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RegionApiService {
    @GET("v1/search/local.json")
    fun getRegionsByKeyword (
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") keyword: String,
        @Query("display") display: Int,
    )  : Call<RestaurantRoot>
}