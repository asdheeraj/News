package com.dheeraj.news.presentation.viewmodel

import androidx.lifecycle.LiveData
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
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _newsArticlesLiveData: MutableLiveData<Resource<List<NewsArticle>>> = MutableLiveData()
    val newsArticlesLiveData : LiveData<Resource<List<NewsArticle>>> = _newsArticlesLiveData

    init {
        getNewsTopHeadlines()
    }

    fun getNewsTopHeadlines() = viewModelScope.launch(dispatcher) {
        with(_newsArticlesLiveData) {
            postValue(Resource.Loading())
            postValue(getNewsTopHeadlinesUseCase.execute())
        }
    }
}