package com.dheeraj.news.domain.entity

data class NewsArticle(
    val description: String,
    val author: String,
    val imageUrl: String
) {
    companion object {
        const val PLACEHOLDER_IMAGE_URL = "https://cbsnews2.cbsistatic.com/hub/i/r/2021/03/23/2fe4a705-1df3-46f4-a7f4-066c90d3ff7b/thumbnail/1200x630/243bbb0c18ab20a652fc73828e2c326c/gettyimages-1308536283.jpg"
    }
}