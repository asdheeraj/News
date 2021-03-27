package com.dheeraj.news.util

/**
 * Takes in the raw [articleId] and returns a modified articleId by
 * removing the scheme and replacing '/' with '-'
 */

fun getArticleId(articleId: String?) =
    articleId?.split("//")?.lastOrNull()?.replace('/', '-')