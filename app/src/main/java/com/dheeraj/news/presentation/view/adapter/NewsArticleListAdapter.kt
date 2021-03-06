package com.dheeraj.news.presentation.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dheeraj.news.R
import com.dheeraj.news.databinding.ItemNewsArticleBinding
import com.dheeraj.news.domain.model.NewsArticle

class NewsArticleListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemNewsArticleBinding: ItemNewsArticleBinding

    private val diffCallback = object : DiffUtil.ItemCallback<NewsArticle>() {

        override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean =
            oldItem.articleId == newItem.articleId

        override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean =
            oldItem == newItem

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        itemNewsArticleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_news_article,
            parent,
            false
        )
        return NewsArticleViewHolder(
            itemNewsArticleBinding.root,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsArticleViewHolder -> {
                holder.bind(itemNewsArticleBinding, differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<NewsArticle>) {
        differ.submitList(list)
    }

    class NewsArticleViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(binding: ItemNewsArticleBinding, item: NewsArticle) = with(itemView) {
            setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            binding.newsArticle = item
            item.imageUrl?.let { url ->
                loadImageUsingGlide(binding.ivNewsArticle, url)
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

    }

    interface Interaction {
        fun onItemSelected(position: Int, item: NewsArticle)
    }
}