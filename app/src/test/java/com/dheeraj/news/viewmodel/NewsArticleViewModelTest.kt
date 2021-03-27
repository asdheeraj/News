package com.dheeraj.news.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dheeraj.news.data.repository.FakeNewsRepository
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase
import com.dheeraj.news.presentation.viewmodel.NewsArticleViewModel
import com.dheeraj.news.util.TestCoroutineRule
import com.dheeraj.news.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class NewsArticleViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var getLikesAndCommentsUseCase: GetLikesAndCommentsUseCase

    private lateinit var fakeNewsRepository: FakeNewsRepository

    private lateinit var newsArticleViewModel: NewsArticleViewModel

    @Before
    fun setUp() {
        fakeNewsRepository = FakeNewsRepository()
        getLikesAndCommentsUseCase = GetLikesAndCommentsUseCase(fakeNewsRepository)
        newsArticleViewModel = NewsArticleViewModel(
            getLikesAndCommentsUseCase,
            testCoroutineRule.testCorutineDispatcher
        )
    }

    @Test
    fun getCommentsAndLikes_expectedResult() {
        testCoroutineRule.runBlockingTest {
            newsArticleViewModel.getLikesAndComments(NewsArticle(articleId = "https://www.cbsnews.com/live-updates/boulder-shooting-colorado-2021-03-23/\""))
            val newsArticle = newsArticleViewModel.newsArticleLiveData.getOrAwaitValue()
            val isDataAvailable = newsArticleViewModel.isDataAvailable.getOrAwaitValue()
            assert(isDataAvailable)
            assert(newsArticle.data?.likes == 56)
            assert(newsArticle.data?.comments == 56)
        }
    }

    @Test
    fun getCommentsAndLikes_errorResult() {
        fakeNewsRepository.setReturnError(true)
        testCoroutineRule.runBlockingTest {
            newsArticleViewModel.getLikesAndComments(NewsArticle())
            val newsArticle = newsArticleViewModel.newsArticleLiveData.getOrAwaitValue()
            val isDataAvailable = newsArticleViewModel.isDataAvailable.getOrAwaitValue()
            assert(!isDataAvailable)
            assert(newsArticle.data?.likes == null && newsArticle.data?.comments == null)
        }
    }
}