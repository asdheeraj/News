package com.dheeraj.news.util

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.dheeraj.news.R
import com.dheeraj.news.presentation.view.NewsActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Helper class to launch Fragment with a sample Android Entry Point Activity for the Fragment to launch
 * Referred from: https://github.com/mitchtabian/Dagger-Hilt-Playerground/blob/hilt-testing/app/src/androidTest/java/com/codingwithmitch/daggerhiltplayground/util/HiltExt.kt
 */

@ExperimentalCoroutinesApi
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    crossinline action: Fragment.() -> Unit = {}
) {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            NewsActivity::class.java
        )
    ).putExtra("androidx.fragment.app.testing.FragmentScenario" +
            ".EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY", themeResId)

    ActivityScenario.launch<NewsActivity>(startActivityIntent).onActivity { activity ->
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs

        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commit()

        fragment.action()
    }
}