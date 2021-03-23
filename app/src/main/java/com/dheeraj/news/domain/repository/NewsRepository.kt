package com.dheeraj.news.domain.repository

import com.dheeraj.news.domain.entity.NewsArticle
import com.dheeraj.news.util.Resource

interface NewsRepository {
    suspend fun getTopHeadlines() : Resource<List<NewsArticle>>
}