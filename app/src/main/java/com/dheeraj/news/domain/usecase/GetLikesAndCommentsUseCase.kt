package com.dheeraj.news.domain.usecase

import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.data.util.Resource
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class GetLikesAndCommentsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend fun execute(article: NewsArticle) =
        getArticleId(article.articleId ?: "")?.let { newsArticleId ->
            newsRepository.getLikes(newsArticleId)
                .zip(newsRepository.getComments(newsArticleId)) { likesResponse, commentsResponse ->
                    return@zip when {
                        likesResponse is Resource.Error && commentsResponse is Resource.Error -> {
                            Resource.Error(
                                message = ERROR_FETCHING_LIKES_AND_COMMENTS,
                                data = article
                            )
                        }
                        likesResponse is Resource.Error -> {
                            article.comments = commentsResponse.data?.comments
                            Resource.Error(
                                message = ERROR_FETCHING_LIKES,
                                data = article
                            )
                        }
                        commentsResponse is Resource.Error -> {
                            article.likes = likesResponse.data?.likes
                            Resource.Error(
                                message = ERROR_FETCHING_COMMENTS,
                                data = article
                            )
                        }
                        else -> {
                            article.apply {
                                likes = likesResponse.data?.likes
                                comments = commentsResponse.data?.comments
                            }
                            Resource.Success(article)
                        }
                    }
                }
        } ?: flowOf(Resource.Error(message = ERROR_FETCHING_LIKES_AND_COMMENTS, data = article))

    companion object {
        const val ERROR_FETCHING_LIKES_AND_COMMENTS = "Error fetching likes and comments"
        const val ERROR_FETCHING_LIKES = "Error fetching Likes"
        const val ERROR_FETCHING_COMMENTS = "Error fetching Comments"
    }

    /**
     * Takes in the raw [articleId] and returns a modified articleId to fetch the Likes and comments
     */
    private fun getArticleId(articleId: String) =
        articleId.split("//").lastOrNull()?.replace('/', '-')
}