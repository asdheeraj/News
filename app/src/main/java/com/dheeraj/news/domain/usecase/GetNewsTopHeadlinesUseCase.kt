package com.dheeraj.news.domain.usecase

import com.dheeraj.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsTopHeadlinesUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun execute() = newsRepository.getTopHeadlines()
}