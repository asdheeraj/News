package com.dheeraj.news.data.repository.dataSource

import com.dheeraj.news.data.model.CommentsResponse
import com.dheeraj.news.data.model.LikesResponse
import com.dheeraj.news.data.model.NewsHeadlinesApiResponse
import com.dheeraj.news.util.Resource

interface NewsRemoteDataSource {

    suspend fun getNewsTopHeadlines(): Resource<NewsHeadlinesApiResponse>

    suspend fun getLikes(articleId: String): Resource<LikesResponse>

    suspend fun getComments(articleId: String): Resource<CommentsResponse>
}