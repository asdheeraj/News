package com.dheeraj.news.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dheeraj.news.R
import com.dheeraj.news.databinding.FragmentNewsBinding
import com.dheeraj.news.domain.entity.NewsArticle
import com.dheeraj.news.presentation.view.adapter.NewsArticleListAdapter
import com.dheeraj.news.presentation.viewmodel.NewsViewModel
import com.dheeraj.news.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(), NewsArticleListAdapter.Interaction {

    private val newsViewModel by lazy {initViewModel()}
    private lateinit var fragmentNewsBinding: FragmentNewsBinding
    private lateinit var newsArticleListAdapter: NewsArticleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNewsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)
        return fragmentNewsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()
    }

    private fun initViewModel(): NewsViewModel {
        return when (activity) {
            is NewsActivity -> {
                (activity as NewsActivity).getViewModelInstance()
            }
            else -> {
                throw IllegalArgumentException("Unknown Activity Instance")
            }
        }
    }

    private fun initViews() {
        newsArticleListAdapter = NewsArticleListAdapter(this@NewsFragment)
        with(fragmentNewsBinding.rvNews) {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsArticleListAdapter
        }
    }

    private fun subscribeObservers() {
        newsViewModel.newsArticlesLiveData.observe(viewLifecycleOwner, Observer { newsResponse ->
            when (newsResponse) {
                is Resource.Success -> {
                    hideProgressBar()
                    setUpUI(newsResponse.data ?: arrayListOf())
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(activity, newsResponse.message ?: "", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setUpUI(articles: List<NewsArticle>) {
        newsArticleListAdapter.submitList(articles)
    }

    private fun showProgressBar() {
        fragmentNewsBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        fragmentNewsBinding.progressBar.visibility = View.GONE
    }

    override fun onItemSelected(position: Int, item: NewsArticle) {
        Toast.makeText(activity, "$position is clicked", Toast.LENGTH_LONG).show()
    }
}