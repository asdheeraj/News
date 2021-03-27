package com.dheeraj.news.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dheeraj.news.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }
}