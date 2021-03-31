package com.dheeraj.news.usecase

import com.dheeraj.news.data.repository.FakeNewsRepository
import com.dheeraj.news.domain.usecase.GetNewsTopHeadlinesUseCase
import com.google.common.truth.Truth.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNewsTopHeadlinesUseCaseTest {
    private lateinit var getNewsTopHeadlinesUseCase: GetNewsTopHeadlinesUseCase
    private lateinit var newsRepository: FakeNewsRepository

    @Before
    fun setUp() {
        newsRepository = FakeNewsRepository()
        getNewsTopHeadlinesUseCase = GetNewsTopHeadlinesUseCase(newsRepository)
    }

    @Test
    fun getNewsTopHeadlines_sentRequest_receivedExpected() {
        runBlocking {
            val response = getNewsTopHeadlinesUseCase.execute()
            assertThat(response.data).isNotNull()
            assertThat(response.message).isNull()
            assertThat(response.data?.size).isEqualTo(2)
        }
    }

    @Test
    fun getNewsTopHeadlines_sentRequest_receivedError() {
        newsRepository.setHeadlinesReturnError(true)
        runBlocking {
            val response = getNewsTopHeadlinesUseCase.execute()
            assertThat(response.data).isNull()
            assertThat(response.message).isEqualTo("Error Fetching Headlines")
        }
    }
}