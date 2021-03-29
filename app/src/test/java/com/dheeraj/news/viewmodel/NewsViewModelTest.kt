package com.dheeraj.news.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dheeraj.news.data.repository.FakeNewsRepository
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase
import com.dheeraj.news.domain.usecase.GetNewsTopHeadlinesUseCase
import com.dheeraj.news.presentation.viewmodel.NewsViewModel
import com.dheeraj.news.util.TestCoroutineRule
import com.dheeraj.news.util.getOrAwaitValue
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
        fakeNewsRepository.setReturnError(true)
        testCoroutineRule.runBlockingTest {
            newsViewModel.getNewsTopHeadlines()
            val newsArticles = newsViewModel.newsArticlesLiveData.getOrAwaitValue()
            assert(newsArticles.data == null)
        }
    }

    @Test
    fun getCommentsAndLikes_expectedResult() {
        testCoroutineRule.runBlockingTest {
            newsViewModel.getLikesAndComments(NewsArticle(articleId = "https://www.cbsnews.com/live-updates/boulder-shooting-colorado-2021-03-23/\""))
            val newsArticle = newsViewModel.newsArticleLiveData.getOrAwaitValue()
            assert(newsArticle.data?.likes == 56)
            assert(newsArticle.data?.comments == 56)
        }
    }

    @Test
    fun getCommentsAndLikes_errorResult() {
        fakeNewsRepository.setReturnError(true)
        testCoroutineRule.runBlockingTest {
            newsViewModel.getLikesAndComments(NewsArticle())
            val newsArticle = newsViewModel.newsArticleLiveData.getOrAwaitValue()
            assert(newsArticle.data?.likes == null && newsArticle.data?.comments == null)
        }
    }

}