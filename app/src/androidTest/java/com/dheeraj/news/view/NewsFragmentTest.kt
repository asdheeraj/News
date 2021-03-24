package com.dheeraj.news.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.dheeraj.news.R
import com.dheeraj.news.data.repository.FakeRepository
import com.dheeraj.news.di.RepositoryModule
import com.dheeraj.news.di.UseCaseModule
import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase
import com.dheeraj.news.domain.usecase.GetNewsTopHeadlinesUseCase
import com.dheeraj.news.presentation.view.NewsActivity
import com.dheeraj.news.presentation.view.NewsFragment
import com.dheeraj.news.presentation.view.adapter.NewsArticleListAdapter.NewsArticleViewHolder
import com.dheeraj.news.util.launchFragmentInHiltContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@UninstallModules(UseCaseModule::class, RepositoryModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
class NewsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var activityScenario: ActivityScenario<NewsActivity>

    @Before
    fun init() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(NewsActivity::class.java)
    }

    @Test
    fun testNewsFragment() {
        val scenario =
            launchFragmentInHiltContainer<NewsFragment>()
    }

    @Test
    fun getNewsArticles_success_visibleRecyclerview() {
        onView(withId(R.id.rv_news))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun getNewsArticles_success_visibleRecyclerview_itemClick() {
        onView(withId(R.id.rv_news))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.rv_news)).perform(
            RecyclerViewActions.scrollToPosition<NewsArticleViewHolder>(1)
            )
    }

    @Test
    fun getNewsArticles_success_visibleRecyclerview_scrollToPosition() {
        onView(withId(R.id.rv_news))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.rv_news)).perform(
            RecyclerViewActions.actionOnItemAtPosition<NewsArticleViewHolder>(
                1, //since the mock response has 25 items, checking the click at position 24
                ViewActions.click()
            )
        )
        onView(withId(R.id.iv_newsArticle_detail))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.tv_news_article))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.tv_news_article_likes))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.tv_news_article_comments))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @InstallIn(SingletonComponent::class)
    @Module
    object TestUseCaseModule {

        @Singleton
        @Provides
        fun provideGetNewsTopHeadlinesUseCase(
            newsRepository: NewsRepository
        ): GetNewsTopHeadlinesUseCase = GetNewsTopHeadlinesUseCase(newsRepository)

        @Singleton
        @Provides
        fun provideGetLikesAndCommentsUseCase(
            newsRepository: NewsRepository
        ): GetLikesAndCommentsUseCase = GetLikesAndCommentsUseCase(newsRepository)

        @Singleton
        @Provides
        fun provideNewsRepository(): NewsRepository = FakeRepository()

    }
}