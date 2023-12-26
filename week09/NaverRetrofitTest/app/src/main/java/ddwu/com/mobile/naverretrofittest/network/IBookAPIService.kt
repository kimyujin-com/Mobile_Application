package ddwu.com.mobile.naverretrofittest.network

import ddwu.com.mobile.naverretrofittest.data.BookRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IBookAPIService {

    @GET("v1/search/book.json")
    fun getBooksByKeyword (
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") keyword: String,
    )  : Call<BookRoot>
}