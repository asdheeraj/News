package com.dheeraj.news.data

import com.dheeraj.news.data.mappers.NewsArticleMapper
import com.dheeraj.news.data.entity.CommentsResponse
import com.dheeraj.news.data.entity.LikesResponse
import com.dheeraj.news.data.repository.dataSource.NewsRemoteDataSource
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class NewsRepoImpl @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsArticleMapper: NewsArticleMapper
) : NewsRepository {

    override suspend fun getTopHeadlines(): Resource<List<NewsArticle>> {
        return newsArticleMapper.mapNewsArticlesResponse(newsRemoteDataSource.getNewsTopHeadlines())
    }

    override suspend fun getLikes(articleId: String): Flow<Resource<LikesResponse>> {
        return flowOf(newsRemoteDataSource.getLikes(articleId))
    }

    override suspend fun getComments(articleId: String): Flow<Resource<CommentsResponse>> {
        return flowOf(newsRemoteDataSource.getComments(articleId))
    }
}