package com.dheeraj.news.api

import com.dheeraj.news.BuildConfig
import com.dheeraj.news.data.api.NewsApiService
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

class NewsApiServiceTest {

    private lateinit var newsApiService: NewsApiService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        newsApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
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
            val response = newsApiService.getNewsTopHeadlines().body()
            val request = mockWebServer.takeRequest()
            assertThat(response).isNotNull()
            assertThat(request.path).isEqualTo("/v2/top-headlines?country=us&apiKey=${BuildConfig.API_KEY}")
            assertThat(response?.articles?.size).isEqualTo(20)
        }
    }

    @Test
    fun getNewsTopHeadlines_receivedResponse_arraySize_expected() {
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val response = newsApiService.getNewsTopHeadlines().body()
            assertThat(response?.articles?.size).isEqualTo(20)
        }
    }

    @Test
    fun getNewsTopHeadlines_receivedResponse_expectedContent() {
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val response = newsApiService.getNewsTopHeadlines().body()
            response?.articles?.first()?.let {article ->
                assertThat(article.author).isEqualTo("Victoria Albert")
                assertThat(article.publishedAt).isEqualTo("2021-03-23T14:22:21Z")
            }
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}