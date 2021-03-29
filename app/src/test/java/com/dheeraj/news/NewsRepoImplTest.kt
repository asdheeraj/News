package com.dheeraj.news

import com.dheeraj.news.data.NewsRepoImpl
import com.dheeraj.news.data.api.NewsApiService
import com.dheeraj.news.data.mappers.NewsArticleMapper
import com.dheeraj.news.data.repository.dataSource.NewsRemoteDataSource
import com.dheeraj.news.data.repository.dataSourceImpl.NewsRemoteDataSourceImpl
import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.data.util.Resource
import com.google.common.truth.Truth.*
import kotlinx.coroutines.flow.collect
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

class NewsRepoImplTest {

    private lateinit var newsRepository: NewsRepository
    private lateinit var newsRemoteDataSource: NewsRemoteDataSource
    private lateinit var mockWebServer: MockWebServer
    private lateinit var newsApiService: NewsApiService
    private lateinit var newsArticleMapper: NewsArticleMapper

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        newsApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
        newsRemoteDataSource = NewsRemoteDataSourceImpl(newsApiService)
        newsArticleMapper = NewsArticleMapper()
        newsRepository = NewsRepoImpl(newsRemoteDataSource, newsArticleMapper)
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
            val response = newsRepository.getTopHeadlines()
            assertThat(response.data).isNotNull()
            assertThat(response.data?.size).isEqualTo(20)
        }
    }

    @Test
    fun getLikes_sentRequest_receivedExpected() {
        runBlocking {
            newsRepository.getLikes(articleId = "www.theverge.com-2020-7-21-21332300-nikon-z5-full-frame-mirrorless-camera-price-release-date-specs-index.html")
                .collect { response ->
                    when (response) {
                        is Resource.Success ->  assertThat(response.data).isNotNull()
                        else -> assertThat(response.message).isNotNull()
                    }
                }
        }
    }

    @Test
    fun getComments_sentRequest_receivedExpected() {
        runBlocking {
            newsRepository.getComments(articleId = "www.theverge.com-2020-7-21-21332300-nikon-z5-full-frame-mirrorless-camera-price-release-date-specs-index.html")
                .collect { response ->
                    when (response) {
                        is Resource.Success ->  assertThat(response.data).isNotNull()
                        else -> assertThat(response.message).isNotNull()
                    }

                }
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}