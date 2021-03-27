package com.dheeraj.news.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dheeraj.news.data.repository.FakeNewsRepository
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

    private lateinit var fakeNewsRepository: FakeNewsRepository

    private lateinit var newsViewModel: NewsViewModel

    @Before
    fun setUp() {
        fakeNewsRepository = FakeNewsRepository()
        getNewsTopHeadlinesUseCase = GetNewsTopHeadlinesUseCase(fakeNewsRepository)
        newsViewModel = NewsViewModel(
            getNewsTopHeadlinesUseCase,
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

}