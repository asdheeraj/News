package com.dheeraj.news.usecase

import com.dheeraj.news.data.repository.FakeNewsRepository
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_COMMENTS
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_LIKES
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_LIKES_AND_COMMENTS
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetLikesAndCommentsUseCaseTest {
    private lateinit var getLikesAndCommentsUseCase: GetLikesAndCommentsUseCase
    private lateinit var newsRepository: FakeNewsRepository

    @Before
    fun setUp() {
        newsRepository = FakeNewsRepository()
        getLikesAndCommentsUseCase = GetLikesAndCommentsUseCase(newsRepository)
    }

    @Test
    fun getLikesAndComments_sentRequest_receivedExpected() {
        runBlocking {
            getLikesAndCommentsUseCase.execute(getNewsArticle()).collect { response ->
                assertThat(response.data).isNotNull()
                assertThat(response.data?.likes).isNotNull()
                assertThat(response.data?.comments).isNotNull()
                assertThat(response.message).isNull()
            }
        }
    }

    @Test
    fun getLikesAndComments_sentRequest_receivedErrorLikes() {
        newsRepository.setLikesReturnError(true)
        runBlocking {
            getLikesAndCommentsUseCase.execute(getNewsArticle()).collect { response ->
                assertThat(response.data).isNotNull()
                assertThat(response.data?.likes).isNull()
                assertThat(response.data?.comments).isNotNull()
                assert(response.message == ERROR_FETCHING_LIKES)
            }
        }
    }

    @Test
    fun getLikesAndComments_sentRequest_receivedErrorComments() {
        newsRepository.setCommentsReturnError(true)
        runBlocking {
            getLikesAndCommentsUseCase.execute(getNewsArticle()).collect { response ->
                assertThat(response.data).isNotNull()
                assertThat(response.data?.likes).isNotNull()
                assertThat(response.data?.comments).isNull()
                assert(response.message == ERROR_FETCHING_COMMENTS)
            }
        }
    }

    @Test
    fun getLikesAndComments_sentRequest_receivedErrorLikesComments() {
        newsRepository.apply {
            setCommentsReturnError(true)
            setLikesReturnError(true)
        }
        runBlocking {
            getLikesAndCommentsUseCase.execute(getNewsArticle()).collect { response ->
                assertThat(response.data).isNotNull()
                assertThat(response.data?.likes).isNull()
                assertThat(response.data?.comments).isNull()
                assert(response.message == ERROR_FETCHING_LIKES_AND_COMMENTS)
            }
        }
    }

    private fun getNewsArticle(): NewsArticle {
        return NewsArticle(
            articleId = "https://www.cbbcnews.com/live-updates/boulder-shooting-colorado-2021-03-23/",
            description = "One of the 10 people killed was a police officer, and a suspect is in custody.",
            author = "Victoria Albert Junior",
            imageUrl = "https://cbsnews2.cbsistatic.com/hub/i/r/2021/03/23/2fe4a705-1df3-46f4-a7f4-066c90d3ff7b/thumbnail/1200x630/243bbb0c18ab20a652fc73828e2c326c/gettyimages-1308536283.jpg"
        )
    }
}