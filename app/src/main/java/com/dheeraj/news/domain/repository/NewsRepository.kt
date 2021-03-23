package com.dheeraj.news.domain.repository

import com.dheeraj.news.domain.entity.Comments
import com.dheeraj.news.domain.entity.Likes
import com.dheeraj.news.domain.entity.NewsArticle
import com.dheeraj.news.util.Resource

interface NewsRepository {
    suspend fun getTopHeadlines() : Resource<List<NewsArticle>>

    suspend fun getLikes() : Resource<Likes>

    suspend fun getComments() : Resource<Comments>
}