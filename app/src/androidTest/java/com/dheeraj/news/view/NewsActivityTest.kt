package com.dheeraj.news.view

import androidx.test.core.app.launchActivity
import com.dheeraj.news.presentation.view.NewsActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NewsActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun newsActivityTest_testLaunch_returnSuccess(){
        val scenario = launchActivity<NewsActivity>()
    }
}