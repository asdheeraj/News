package com.dheeraj.news.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dheeraj.news.data.util.Resource
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.domain.usecase.GetLikesAndCommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticleViewModel @Inject constructor(
    private val getLikesAndCommentsUseCase: GetLikesAndCommentsUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _newsArticleLiveData: MutableLiveData<Resource<NewsArticle>> = MutableLiveData()
    val newsArticleLiveData: LiveData<Resource<NewsArticle>> = _newsArticleLiveData

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    fun getLikesAndComments(article: NewsArticle): Job = viewModelScope.launch(dispatcher) {
        with(_newsArticleLiveData) {
            if (_isDataAvailable.value != true) {
                postValue(Resource.Loading())
                getLikesAndCommentsUseCase.execute(article).collect { articleResponse ->
                    _isDataAvailable.postValue(articleResponse.data?.likes != null && articleResponse.data.comments != null)
                    postValue(articleResponse)
                }
            }
        }
    }
}