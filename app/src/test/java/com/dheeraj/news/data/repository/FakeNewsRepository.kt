package com.dheeraj.news.data.repository

import com.dheeraj.news.data.model.Article
import com.dheeraj.news.data.model.CommentsResponse
import com.dheeraj.news.data.model.LikesResponse
import com.dheeraj.news.data.model.NewsHeadlinesApiResponse
import com.dheeraj.news.data.repository.dataSource.NewsRemoteDataSource
import com.dheeraj.news.domain.entity.NewsArticle
import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNewsRepository : NewsRepository {

    override suspend fun getTopHeadlines(): Resource<List<NewsArticle>> {
        return getFakeApiResponse()
    }

    override suspend fun getLikes(articleId: String): Flow<Resource<LikesResponse>> {
        return flowOf(getFakeLikesResponse())
    }

    override suspend fun getComments(articleId: String): Flow<Resource<CommentsResponse>> {
        return flowOf(getFakeCommentsResponse())
    }

    private fun getFakeApiResponse(): Resource<List<NewsArticle>> {

        return Resource.Success(data = arrayListOf(
            NewsArticle(),
            NewsArticle()
        ))
    }

    private fun getFakeLikesResponse(): Resource<LikesResponse> {
        return Resource.Success(data = LikesResponse(likes = 56))
    }

    private fun getFakeCommentsResponse(): Resource<CommentsResponse> {
        return Resource.Success(data = CommentsResponse(comments = 56))
    }
}