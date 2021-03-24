package com.dheeraj.news.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase
import com.dheeraj.news.domain.usecase.GetNewsTopHeadlinesUseCase
import com.dheeraj.news.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsTopHeadlinesUseCase: GetNewsTopHeadlinesUseCase,
    private val getLikesAndCommentsUseCase: GetLikesAndCommentsUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val newsArticlesLiveData: MutableLiveData<Resource<List<NewsArticle>>> = MutableLiveData()
    val newsArticleLiveData: MutableLiveData<Resource<NewsArticle>> = MutableLiveData()

    init {
        getNewsTopHeadlines()
    }

    fun getNewsTopHeadlines() = viewModelScope.launch(dispatcher) {
        with(newsArticlesLiveData) {
            postValue(Resource.Loading())
            postValue(getNewsTopHeadlinesUseCase.execute())
        }
    }

    fun getLikesAndComments(article: NewsArticle) = viewModelScope.launch(dispatcher) {
        with(newsArticleLiveData) {
            postValue(Resource.Loading())
            getLikesAndCommentsUseCase.execute(article).collect { articleResponse ->
                postValue(articleResponse)
            }
        }
    }
}