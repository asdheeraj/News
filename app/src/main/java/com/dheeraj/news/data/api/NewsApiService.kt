package com.dheeraj.news.data.api

import com.dheeraj.news.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsApiService {

    @GET("v2/top-headlines")
    fun getNewsTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    )

    @GET
    fun getLikes(@Url url: String)

    @GET
    fun getComments(@Url url: String)
}