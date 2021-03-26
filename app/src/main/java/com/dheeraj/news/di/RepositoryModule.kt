package com.dheeraj.news.di

import com.dheeraj.news.data.NewsRepoImpl
import com.dheeraj.news.data.mappers.NewsArticleMapper
import com.dheeraj.news.data.repository.dataSource.NewsRemoteDataSource
import com.dheeraj.news.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(newsRemoteDataSource: NewsRemoteDataSource, newsArticleMapper: NewsArticleMapper): NewsRepository =
        NewsRepoImpl(newsRemoteDataSource, newsArticleMapper)
}