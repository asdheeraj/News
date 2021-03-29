package com.dheeraj.news.di

import com.dheeraj.news.domain.repository.NewsRepository
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase
import com.dheeraj.news.domain.usecase.GetNewsTopHeadlinesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Singleton
    @Provides
    fun provideGetNewsTopHeadlinesUseCase(
        newsRepository: NewsRepository
    ): GetNewsTopHeadlinesUseCase = GetNewsTopHeadlinesUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideGetLikesAndCommentsUseCase(
        newsRepository: NewsRepository
    ): GetLikesAndCommentsUseCase = GetLikesAndCommentsUseCase(newsRepository)

}