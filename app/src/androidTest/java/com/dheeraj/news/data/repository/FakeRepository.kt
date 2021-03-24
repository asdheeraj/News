package com.dheeraj.news.data.repository

import com.dheeraj.news.data.model.CommentsResponse
import com.dheeraj.news.data.model.LikesResponse
import com.dheeraj.news.domain.entity.NewsArticle
import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository : NewsRepository {

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
            NewsArticle(articleId = "https://www.cbsnews.com/live-updates/boulder-shooting-colorado-2021-03-23/",
            description = "One of the 10 people killed was a police officer, and a suspect is in custody.",
            author = "Victoria Albert",
            imageUrl = "https://cbsnews2.cbsistatic.com/hub/i/r/2021/03/23/2fe4a705-1df3-46f4-a7f4-066c90d3ff7b/thumbnail/1200x630/243bbb0c18ab20a652fc73828e2c326c/gettyimages-1308536283.jpg"),
            NewsArticle(articleId = "https://www.cbbcnews.com/live-updates/boulder-shooting-colorado-2021-03-23/",
                description = "One of the 10 people killed was a police officer, and a suspect is in custody.",
                author = "Victoria Albert Junior",
                imageUrl = "https://cbsnews2.cbsistatic.com/hub/i/r/2021/03/23/2fe4a705-1df3-46f4-a7f4-066c90d3ff7b/thumbnail/1200x630/243bbb0c18ab20a652fc73828e2c326c/gettyimages-1308536283.jpg")
        ))
    }

    private fun getFakeLikesResponse(): Resource<LikesResponse> {
        return Resource.Success(data = LikesResponse(likes = 56))
    }

    private fun getFakeCommentsResponse(): Resource<CommentsResponse> {
        return Resource.Success(data = CommentsResponse(comments = 56))
    }
}