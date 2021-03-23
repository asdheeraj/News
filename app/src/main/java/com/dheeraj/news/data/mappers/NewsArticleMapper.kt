package com.dheeraj.news.data.mappers

import com.dheeraj.news.data.model.Article
import com.dheeraj.news.domain.entity.NewsArticle
import com.dheeraj.news.util.EntityMapper

class NewsArticleMapper : EntityMapper<NewsArticle, Article> {

    override fun mapToEntity(model: Article): NewsArticle {
        return NewsArticle(
            articleId = model.url,
            description = model.description,
            author = model.author,
            imageUrl = model.urlToImage ?: NewsArticle.PLACEHOLDER_IMAGE_URL
        )
    }
}