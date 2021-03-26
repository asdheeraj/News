package com.dheeraj.news.data.mapper

import com.dheeraj.news.data.entity.Article
import com.dheeraj.news.data.entity.NewsHeadlinesApiResponse
import com.dheeraj.news.data.entity.Source
import com.dheeraj.news.data.mappers.NewsArticleMapper
import com.dheeraj.news.data.util.Resource
import org.junit.Test

class NewsArticleMapperTest {

    private val newsArticleMapper = NewsArticleMapper()

    fun provideSuccessData() = Resource.Success(
        data = NewsHeadlinesApiResponse(
            articles = arrayListOf(
                Article(
                    author = "Author 1",
                    content = "Content 1",
                    description = "Description 1",
                    publishedAt = "PublishedAt 1",
                    source = Source(
                        id = "SourceId1",
                        name = "SourceName1"
                    ),
                    title = "Title 1",
                    url = "url1",
                    urlToImage = "URLToImage1"
                ),
                Article(
                    author = "Author 2",
                    content = "Content 2",
                    description = "Description 2",
                    publishedAt = "PublishedAt 2",
                    source = Source(
                        id = "SourceId2",
                        name = "SourceName2"
                    ),
                    title = "Title 2",
                    url = "url2",
                    urlToImage = "URLToImage2"
                ),
                Article(
                    author = "Author 2",
                    content = "Content 2",
                    description = "Description 2",
                    publishedAt = "PublishedAt 2",
                    source = Source(
                        id = "SourceId2",
                        name = "SourceName2"
                    ),
                    title = "Title 2",
                    url = "url2",
                    urlToImage = "URLToImage2"
                ),
            )
        )
    )

    fun provideErrorData() = Resource.Error(message = ERROR_MESSAGE, data = NewsHeadlinesApiResponse())

    @Test
    fun mapNewsArticles_SuccessResponse() {
        val response = provideSuccessData()
        val newsArticlesResponse = newsArticleMapper.mapNewsArticlesResponse(response)
        assert(!newsArticlesResponse.data.isNullOrEmpty())
        assert(newsArticlesResponse.data?.firstOrNull()?.author == response.data?.articles?.firstOrNull()?.author)
    }

    @Test
    fun mapNewsArticles_failureResponse() {
        val response = provideErrorData()
        val newsArticlesResponse = newsArticleMapper.mapNewsArticlesResponse(response)
        assert(newsArticlesResponse.data.isNullOrEmpty())
        assert(newsArticlesResponse.message == ERROR_MESSAGE)
    }

    companion object {
        const val ERROR_MESSAGE =  "Error fetching Top headlines"
    }
}