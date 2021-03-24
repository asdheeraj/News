package com.dheeraj.news.domain.repository

import com.dheeraj.news.data.entity.CommentsResponse
import com.dheeraj.news.data.entity.LikesResponse
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines() : Resource<List<NewsArticle>>

    suspend fun getLikes(articleId: String) : Flow<Resource<LikesResponse>>

    suspend fun getComments(articleId: String) : Flow<Resource<CommentsResponse>>
}