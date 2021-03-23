package com.dheeraj.news.data.api

import com.dheeraj.news.BuildConfig
import com.dheeraj.news.data.model.CommentsResponse
import com.dheeraj.news.data.model.LikesResponse
import com.dheeraj.news.data.model.NewsHeadlinesApiResponse
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