package com.dheeraj.news.domain.usecase

import com.dheeraj.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetLikesUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun execute() = newsRepository.getLikes()
}