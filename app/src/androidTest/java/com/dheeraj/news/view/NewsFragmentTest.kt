package com.dheeraj.news.view

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import com.dheeraj.news.presentation.view.NewsActivity
import com.dheeraj.news.presentation.view.NewsFragment
import com.dheeraj.news.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        val scenario = launchFragmentInHiltContainer<NewsFragment> (  )
    }
}