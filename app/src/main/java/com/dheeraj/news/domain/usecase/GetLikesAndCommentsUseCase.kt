package com.dheeraj.news.domain.usecase

import com.dheeraj.news.domain.entity.NewsArticle
import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.util.Resource
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class GetLikesAndCommentsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend fun execute(article: NewsArticle) =
        getArticleId(article.articleId)?.let { newsArticleId ->
            newsRepository.getLikes(newsArticleId)
                .zip(newsRepository.getComments(newsArticleId)) { likes, comments ->
                    return@zip if (likes is Resource.Error || comments is Resource.Error) {
                        Resource.Error(
                            message = ERROR_FETCHING_LIKES_AND_COMMENTS,
                            data = article
                        )
                    } else {
                        article.apply {
                            this.likes = likes.data?.likes
                            this.comments = comments.data?.comments
                        }
                        Resource.Success(article)
                    }
                }
        } ?: flowOf(Resource.Error(message = ERROR_FETCHING_LIKES_AND_COMMENTS, data = article))

    companion object {
        const val ERROR_FETCHING_LIKES_AND_COMMENTS = "Error fetching likes and comments"
    }

    /**
     * Takes in the raw [articleId] and returns a modified
     */
    private fun getArticleId(articleId: String) =
        articleId.split("//").lastOrNull()?.replace('/', '-')
}