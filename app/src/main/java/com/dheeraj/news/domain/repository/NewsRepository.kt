package com.dheeraj.news.domain.repository

import com.dheeraj.news.data.model.CommentsResponse
import com.dheeraj.news.data.model.LikesResponse
import com.dheeraj.news.domain.entity.NewsArticle
import com.dheeraj.news.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines() : Resource<List<NewsArticle>>

    suspend fun getLikes(articleId: String) : Flow<Resource<LikesResponse>>

    suspend fun getComments(articleId: String) : Flow<Resource<CommentsResponse>>
}