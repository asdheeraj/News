package com.dheeraj.news.data.mappers

import com.dheeraj.news.data.entity.Article
import com.dheeraj.news.data.entity.NewsHeadlinesApiResponse
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.data.util.EntityMapper
import com.dheeraj.news.data.util.Resource

class NewsArticleMapper : EntityMapper<NewsArticle, Article> {

    override fun mapFromEntity(entity: Article): NewsArticle {
        return NewsArticle(
            articleId = entity.url,
            description = entity.description,
            author = entity.author,
            imageUrl = entity.urlToImage ?: NewsArticle.PLACEHOLDER_IMAGE_URL
        )
    }

    fun mapNewsArticlesResponse(newsHeadlinesApiResponse: Resource<NewsHeadlinesApiResponse>): Resource<List<NewsArticle>> {
        return newsHeadlinesApiResponse.data?.articles?.let {
            Resource.Success(it.map { article ->
                mapFromEntity(article)
            })
        } ?: Resource.Error(
            message = newsHeadlinesApiResponse.message ?: "Error Fetching Top Headlines"
        )
    }
}