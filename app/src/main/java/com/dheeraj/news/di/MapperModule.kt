package com.dheeraj.news.di

import com.dheeraj.news.data.mappers.NewsArticleMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MapperModule {

    @Singleton
    @Provides
    fun provideNewsArticleMapper() = NewsArticleMapper()
}