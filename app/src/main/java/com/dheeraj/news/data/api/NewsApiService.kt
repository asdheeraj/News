package com.dheeraj.news.data.api

import com.dheeraj.news.BuildConfig
import com.dheeraj.news.data.entity.CommentsResponse
import com.dheeraj.news.data.entity.LikesResponse
import com.dheeraj.news.data.entity.NewsHeadlinesApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsApiService {

    @GET("v2/top-headlines")
    suspend fun getNewsTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ) : Response<NewsHeadlinesApiResponse>

    @GET
    suspend fun getLikes(@Url url: String) : Response<LikesResponse>

    @GET
    suspend fun getComments(@Url url: String) : Response<CommentsResponse>
}