package com.dheeraj.news.util

import org.junit.Test

class StringUtilsTest {

    @Test
    fun expectedArticleId_parse_expectedResult() {
        val articleId = "https://www.cbsnews.com/live-updates/boulder-shooting-colorado-2021-03-23/"
        assert(getArticleId(articleId) == "www.cbsnews.com-live-updates-boulder-shooting-colorado-2021-03-23-")
    }

    @Test
    fun nullArticleId_parse_nullResult() {
        assert(getArticleId(null) == null)
    }
}