package com.dheeraj.news.data.repository.dataSourceImpl

import com.dheeraj.news.data.api.NewsApiService
import com.dheeraj.news.data.model.CommentsResponse
import com.dheeraj.news.data.model.LikesResponse
import com.dheeraj.news.data.model.NewsHeadlinesApiResponse
import com.dheeraj.news.data.repository.dataSource.NewsRemoteDataSource
import com.dheeraj.news.util.Resource
import com.dheeraj.news.util.safeApiCall
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(
    private val newsApiService: NewsApiService
) : NewsRemoteDataSource {

    override suspend fun getNewsTopHeadlines(): Resource<NewsHeadlinesApiResponse> {
        return safeApiCall {
            newsApiService.getNewsTopHeadlines()
        }
    }

    override suspend fun getLikes(articleId: String): Resource<LikesResponse> {
        return safeApiCall {
            newsApiService.getLikes(url = "$BASE_URL_LIKES$articleId")
        }
    }

    override suspend fun getComments(articleId: String): Resource<CommentsResponse> {
        return safeApiCall {
            newsApiService.getComments(url = "$BASE_URL_COMMENTS$articleId")
        }
    }

    companion object {
        const val BASE_URL_LIKES = "https://cn-news-info-api.herokuapp.com/likes/"
        const val BASE_URL_COMMENTS = "https://cn-news-info-api.herokuapp.com/comments/"
    }

}