package com.dheeraj.news.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dheeraj.news.R
import com.dheeraj.news.databinding.FragmentNewsArticleBinding
import com.dheeraj.news.domain.model.NewsArticle
import com.dheeraj.news.presentation.viewmodel.NewsViewModel
import com.dheeraj.news.data.util.Resource
import com.dheeraj.news.presentation.viewmodel.NewsArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsArticleFragment : Fragment() {

    private val args: NewsArticleFragmentArgs by navArgs()
    private val newsArticleViewModel: NewsArticleViewModel by viewModels()
    private lateinit var newsArticleBinding: FragmentNewsArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newsArticleBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_news_article, container, false)
        newsArticleViewModel.getLikesAndComments(args.newsArticle)
        return newsArticleBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        newsArticleViewModel.newsArticleLiveData.observe(viewLifecycleOwner, Observer { newsResponse ->
            when (newsResponse) {
                is Resource.Success -> {
                    hideProgressBar()
                    setUpUI(newsResponse.data)
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    setUpUI(newsResponse.data)
                    Toast.makeText(activity, newsResponse.message ?: "", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setUpUI(article: NewsArticle?) {
        newsArticleBinding.apply {
            article?.imageUrl?.let { url ->
                loadImageUsingGlide(ivNewsArticleDetail, url)
            }
            newsArticle = article
        }
    }

    private fun loadImageUsingGlide(imageView: ImageView, avatarURL: String) {
        Glide.with(imageView.context)
            .load(avatarURL)
            .apply(
                RequestOptions().placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
            )
            .into(imageView)
    }

    private fun showProgressBar() {
        newsArticleBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        newsArticleBinding.progressBar.visibility = View.GONE
    }
}