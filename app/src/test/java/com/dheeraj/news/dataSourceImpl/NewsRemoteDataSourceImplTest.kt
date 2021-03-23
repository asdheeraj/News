package com.dheeraj.news.dataSourceImpl

import com.dheeraj.news.BuildConfig
import com.dheeraj.news.data.api.NewsApiService
import com.dheeraj.news.data.repository.dataSource.NewsRemoteDataSource
import com.dheeraj.news.data.repository.dataSourceImpl.NewsRemoteDataSourceImpl
import com.dheeraj.news.util.Resource
import com.google.common.truth.Truth
import com.google.common.truth.Truth.*
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRemoteDataSourceImplTest {

    private lateinit var newsRemoteDataSource: NewsRemoteDataSource
    private lateinit var mockWebServer: MockWebServer
    private lateinit var newsApiService: NewsApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        newsApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
        newsRemoteDataSource = NewsRemoteDataSourceImpl(newsApiService)
    }

    private fun enqueueMockResponse(fileName: String) {
        javaClass.classLoader?.getResourceAsStream(fileName)?.let { inputStream ->
            val mockResponse = MockResponse().apply {
                setBody(inputStream.source().buffer().readString(Charsets.UTF_8))
            }
            mockWebServer.enqueue(mockResponse)
        }
    }

    @Test
    fun getNewsTopHeadlines_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val response = newsRemoteDataSource.getNewsTopHeadlines()
            assertThat(response.data).isNotNull()
            assertThat(response.data?.articles?.size).isEqualTo(20)
        }
    }

    @Test
    fun getLikes_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("likes.json")
            val response = newsRemoteDataSource.getLikes(articleId = "www.theverge.com-2020-7-21-21332300-nikon-z5-full-frame-mirrorless-camera-price-release-date-specs-index.html")
            assertThat(response.data).isNotNull()
        }
    }

    @Test
    fun getComments_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("comments.json")
            val response = newsRemoteDataSource.getComments(articleId = "www.theverge.com-2020-7-21-21332300-nikon-z5-full-frame-mirrorless-camera-price-release-date-specs-index.html")
            assertThat(response.data).isNotNull()
        }
    }

}