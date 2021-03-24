package com.dheeraj.news.usecase

import com.dheeraj.news.data.NewsRepoImpl
import com.dheeraj.news.data.api.NewsApiService
import com.dheeraj.news.data.repository.dataSource.NewsRemoteDataSource
import com.dheeraj.news.data.repository.dataSourceImpl.NewsRemoteDataSourceImpl
import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_COMMENTS
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_LIKES
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_LIKES_AND_COMMENTS
import com.dheeraj.news.data.util.Resource
import com.google.common.truth.Truth.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetLikesAndCommentsUseCaseTest {
    private lateinit var getLikesAndCommentsUseCase: GetLikesAndCommentsUseCase
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
        getLikesAndCommentsUseCase = GetLikesAndCommentsUseCase(newsRepository)
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
    fun getLikesAndComments_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("newsresponse.json")
             newsRepository.getTopHeadlines().data?.last()?.let { newsArticle ->
                 getLikesAndCommentsUseCase.execute(newsArticle).collect { response ->
                     when (response) {
                         is Resource.Success -> {
                             assertThat(response.data).isNotNull()
                             assertThat(response.data?.likes).isNotNull()
                             assertThat(response.data?.comments).isNotNull()
                         }
                         else -> {
                             assert(response.message == ERROR_FETCHING_LIKES_AND_COMMENTS || response.message ==
                             ERROR_FETCHING_LIKES || response.message == ERROR_FETCHING_COMMENTS)
                         }
                     }
                 }
            }

        }
    }
}