package com.dheeraj.news.data.repository.dataSource

import com.dheeraj.news.data.entity.CommentsResponse
import com.dheeraj.news.data.entity.LikesResponse
import com.dheeraj.news.data.entity.NewsHeadlinesApiResponse
import com.dheeraj.news.data.util.Resource

interface NewsRemoteDataSource {

    suspend fun getNewsTopHeadlines(): Resource<NewsHeadlinesApiResponse>

    suspend fun getLikes(articleId: String): Resource<LikesResponse>

    suspend fun getComments(articleId: String): Resource<CommentsResponse>
}