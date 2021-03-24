package com.dheeraj.news.data.mappers

import com.dheeraj.news.data.entity.Article
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.data.util.EntityMapper

class NewsArticleMapper : EntityMapper<NewsArticle, Article> {

    override fun mapFromEntity(entity: Article): NewsArticle {
        return NewsArticle(
            articleId = entity.url,
            description = entity.description,
            author = entity.author,
            imageUrl = entity.urlToImage ?: NewsArticle.PLACEHOLDER_IMAGE_URL
        )
    }
}