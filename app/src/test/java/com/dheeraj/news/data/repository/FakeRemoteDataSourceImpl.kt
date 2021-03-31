package com.dheeraj.news.data.repository

import com.dheeraj.news.data.api.NewsApiService
import com.dheeraj.news.data.entity.CommentsResponse
import com.dheeraj.news.data.entity.LikesResponse
import com.dheeraj.news.data.entity.NewsHeadlinesApiResponse
import com.dheeraj.news.data.repository.dataSource.NewsRemoteDataSource
import com.dheeraj.news.data.util.Resource
import com.dheeraj.news.data.util.safeApiCall
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_COMMENTS
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase.Companion.ERROR_FETCHING_LIKES

class FakeRemoteDataSourceImpl(private val newsApiService: NewsApiService) : NewsRemoteDataSource {

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getNewsTopHeadlines(): Resource<NewsHeadlinesApiResponse> {
        return getFakeApiResponse()
    }

    override suspend fun getLikes(articleId: String): Resource<LikesResponse> {
        return getFakeLikesResponse(articleId)
    }

    override suspend fun getComments(articleId: String): Resource<CommentsResponse> {
        return getFakeCommentsResponse(articleId)
    }

    private suspend fun getFakeApiResponse(): Resource<NewsHeadlinesApiResponse> {
        return if (shouldReturnError) {
            Resource.Error(message = "Error Fetching Headlines", data = null)
        } else {
            safeApiCall {
                newsApiService.getNewsTopHeadlines()
            }
        }
    }

    private suspend fun getFakeLikesResponse(articleId: String): Resource<LikesResponse> {
        return if (shouldReturnError) {
            Resource.Error(message = ERROR_FETCHING_LIKES)
        } else {
            safeApiCall {
                newsApiService.getLikes(articleId)
            }
        }
    }

    private suspend fun getFakeCommentsResponse(articleId: String): Resource<CommentsResponse> {
        return if (shouldReturnError) {
            Resource.Error(message = ERROR_FETCHING_COMMENTS)
        } else {
            safeApiCall {
                newsApiService.getComments(articleId)
            }
        }
    }

}