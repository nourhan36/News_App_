package com.example.newsapp.ui.articleDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.api.Constants
import com.example.newsapp.api.model.newsResponse.Article
import com.example.newsapp.databinding.FragmentArticleDetailsBinding

class ArticleDetailsFragment : Fragment() {
    private lateinit var viewBinding: FragmentArticleDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = arguments?.getParcelable<Article>(Constants.ARTICLE)
        article?.let { bindArticle(it) }
    }

    private fun bindArticle(article: Article) {
        viewBinding.articleTitle.text = article.title
        Glide.with(this)
            .load(article.urlToImage)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(viewBinding.articleImage)
        viewBinding.articlePublishAt.text = article.publishedAt
        viewBinding.tvArticleBody.text = article.description
        viewBinding.tvViewArticle.setOnClickListener {
            openArticleUrl(article.url)
        }
    }

    private fun openArticleUrl(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}