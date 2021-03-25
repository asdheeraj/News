package com.dheeraj.news.usecase

import com.dheeraj.news.data.NewsRepoImpl
import com.dheeraj.news.data.api.NewsApiService
import com.dheeraj.news.data.repository.dataSource.NewsRemoteDataSource
import com.dheeraj.news.data.repository.dataSourceImpl.NewsRemoteDataSourceImpl
import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.domain.usecase.GetNewsTopHeadlinesUseCase
import com.google.common.truth.Truth.*
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

class GetNewsTopHeadlinesUseCaseTest {
    private lateinit var getNewsTopHeadlinesUseCase: GetNewsTopHeadlinesUseCase
    private lateinit var newsRepository: NewsRepository
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
        newsRepository = NewsRepoImpl(newsRemoteDataSource)
        getNewsTopHeadlinesUseCase = GetNewsTopHeadlinesUseCase(newsRepository)
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
            val response = getNewsTopHeadlinesUseCase.execute()
            assertThat(response.data).isNotNull()
            assertThat(response.data?.size).isEqualTo(20)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}