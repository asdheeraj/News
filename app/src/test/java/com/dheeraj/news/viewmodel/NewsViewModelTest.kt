package com.dheeraj.news.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dheeraj.news.data.repository.FakeNewsRepository
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_COMMENTS
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_LIKES
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_LIKES_AND_COMMENTS
import com.dheeraj.news.domain.usecase.GetNewsTopHeadlinesUseCase
import com.dheeraj.news.presentation.viewmodel.NewsViewModel
import com.dheeraj.news.util.TestCoroutineRule
import com.dheeraj.news.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class NewsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var getNewsTopHeadlinesUseCase: GetNewsTopHeadlinesUseCase

    private lateinit var getLikesAndCommentsUseCase: GetLikesAndCommentsUseCase

    private lateinit var fakeNewsRepository: FakeNewsRepository

    private lateinit var newsViewModel: NewsViewModel

    @Before
    fun setUp() {
        fakeNewsRepository = FakeNewsRepository()
        getNewsTopHeadlinesUseCase = GetNewsTopHeadlinesUseCase(fakeNewsRepository)
        getLikesAndCommentsUseCase = GetLikesAndCommentsUseCase(fakeNewsRepository)
        newsViewModel = NewsViewModel(
            getNewsTopHeadlinesUseCase,
            getLikesAndCommentsUseCase,
            testCoroutineRule.testCorutineDispatcher
        )
    }

    @Test
    fun getTopHeadlines_expectedResult() {
        testCoroutineRule.runBlockingTest {
            newsViewModel.getNewsTopHeadlines()
            val newsArticles = newsViewModel.newsArticlesLiveData.getOrAwaitValue()
            assert(newsArticles.data?.size == 2)
        }
    }

    @Test
    fun getTopHeadlines_errorResult() {
        fakeNewsRepository.setHeadlinesReturnError(true)
        testCoroutineRule.runBlockingTest {
            newsViewModel.getNewsTopHeadlines()
            val newsArticles = newsViewModel.newsArticlesLiveData.getOrAwaitValue()
            assertThat(newsArticles.data).isNull()
            assertThat(newsArticles.message).isEqualTo("Error Fetching Headlines")
        }
    }

    @Test
    fun getCommentsAndLikes_expectedResult() {
        testCoroutineRule.runBlockingTest {
            newsViewModel.getLikesAndComments(getNewsArticle())
            val newsArticle = newsViewModel.newsArticleLiveData.getOrAwaitValue()
            assert(newsArticle.data?.likes == 56)
            assert(newsArticle.data?.comments == 56)
        }
    }

    @Test
    fun getCommentsAndLikes_errorResult_likes() {
        fakeNewsRepository.setLikesReturnError(true)
        testCoroutineRule.runBlockingTest {
            newsViewModel.getLikesAndComments(getNewsArticle())
            val newsArticle = newsViewModel.newsArticleLiveData.getOrAwaitValue()
            assertThat(newsArticle.data).isNotNull()
            assertThat(newsArticle.data?.likes).isNull()
            assertThat(newsArticle.data?.comments).isNotNull()
            assertThat(newsArticle.data?.comments).isEqualTo(56)
            assertThat(newsArticle.message).isEqualTo(ERROR_FETCHING_LIKES)
        }
    }

    @Test
    fun getCommentsAndLikes_errorResult_comments() {
        fakeNewsRepository.setCommentsReturnError(true)
        testCoroutineRule.runBlockingTest {
            newsViewModel.getLikesAndComments(getNewsArticle())
            val newsArticle = newsViewModel.newsArticleLiveData.getOrAwaitValue()
            assertThat(newsArticle.data).isNotNull()
            assertThat(newsArticle.data?.likes).isNotNull()
            assertThat(newsArticle.data?.comments).isNull()
            assertThat(newsArticle.data?.likes).isEqualTo(56)
            assertThat(newsArticle.message).isEqualTo(ERROR_FETCHING_COMMENTS)
        }
    }

    @Test
    fun getCommentsAndLikes_errorResult_commentsAndLikes() {
        fakeNewsRepository.apply {
            setLikesReturnError(true)
            setCommentsReturnError(true)
        }
        testCoroutineRule.runBlockingTest {
            newsViewModel.getLikesAndComments(getNewsArticle())
            val newsArticle = newsViewModel.newsArticleLiveData.getOrAwaitValue()
            assertThat(newsArticle.data).isNotNull()
            assertThat(newsArticle.data?.likes).isNull()
            assertThat(newsArticle.data?.comments).isNull()
            assertThat(newsArticle.message).isEqualTo(ERROR_FETCHING_LIKES_AND_COMMENTS)
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