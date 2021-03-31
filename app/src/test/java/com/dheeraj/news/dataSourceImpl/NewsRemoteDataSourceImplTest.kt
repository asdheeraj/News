package com.dheeraj.news.dataSourceImpl

import com.dheeraj.news.data.api.NewsApiService
import com.dheeraj.news.data.repository.FakeRemoteDataSourceImpl
import com.dheeraj.news.data.util.Resource
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_COMMENTS
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_LIKES
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRemoteDataSourceImplTest {

    private lateinit var newsRemoteDataSource: FakeRemoteDataSourceImpl
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
        newsRemoteDataSource = FakeRemoteDataSourceImpl(newsApiService)
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
    fun getNewsTopHeadlines_sentRequest_receivedError() {
        newsRemoteDataSource.setReturnError(true)
        runBlocking {
            val response = newsRemoteDataSource.getNewsTopHeadlines()
            assertThat(response.data).isNull()
            assertThat(response.message).isEqualTo("Error Fetching Headlines")
        }
    }

    @Test
    fun getLikes_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("likes.json")
            when (val response = newsRemoteDataSource.getLikes(articleId = "www.theverge.com-2020-7-21-21332300-nikon-z5-full-frame-mirrorless-camera-price-release-date-specs-index.html")) {
                is Resource.Success ->  assertThat(response.data).isNotNull()
                else -> assertThat(response.message).isEqualTo("Error")
            }
        }
    }

    @Test
    fun getLikes_sentRequest_receivedError() {
        newsRemoteDataSource.setReturnError(true)
        runBlocking {
            val response = newsRemoteDataSource.getLikes(articleId = "www.theverge.com-2020-7-21-21332300-nikon-z5-full-frame-mirrorless-camera-price-release-date-specs-index.html")
            assertThat(response.data).isNull()
            assertThat(response.message).isEqualTo(ERROR_FETCHING_LIKES)
        }
    }

    @Test
    fun getComments_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("comments.json")
            when (val response = newsRemoteDataSource.getComments(articleId = "www.theverge.com-2020-7-21-21332300-nikon-z5-full-frame-mirrorless-camera-price-release-date-specs-index.html")) {
                is Resource.Success ->  assertThat(response.data).isNotNull()
                else -> assertThat(response.message).isNotNull()
            }
        }
    }

    @Test
    fun getComments_sentRequest_receivedError() {
        newsRemoteDataSource.setReturnError(true)
        runBlocking {
            val response = newsRemoteDataSource.getComments(articleId = "www.theverge.com-2020-7-21-21332300-nikon-z5-full-frame-mirrorless-camera-price-release-date-specs-index.html")
            assertThat(response.data).isNull()
            assertThat(response.message).isEqualTo(ERROR_FETCHING_COMMENTS)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

}