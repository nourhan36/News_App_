package com.example.newsapp.newsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.ViewMessage
import com.example.newsapp.api.Constants
import com.example.newsapp.api.model.newsResponse.Article
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.articleDetails.ArticleDetailsFragment
import com.example.newsapp.ui.MainActivity

class NewsFragment : Fragment() {
    private lateinit var viewBinding: FragmentNewsBinding
    private lateinit var viewModel: NewsViewModel
    private val articleDetailsFragment = ArticleDetailsFragment()
    private var source: Source? = null
    private val adapter = NewsAdapter(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentNewsBinding.inflate(
            inflater,
            container,
            false
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSearch()
        initArticle()
        observeLiveData()
    }

    private fun initViews() {
        viewBinding.newsRecycler.adapter = adapter
    }

    private fun initSearch() {
        (activity as MainActivity).setOnSearchClickListener { query ->
            viewModel.loadArticles(query)
        }
    }

    private fun initArticle() {
        adapter.setOnArticleClickListener{article ->
            val arguments = Bundle().apply {
                putParcelable(Constants.ARTICLE, article)
            }
            articleDetailsFragment.arguments = arguments

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, articleDetailsFragment)
                .addToBackStack("")
                .commit()
        }
    }

    private fun observeLiveData() {
        viewModel.isLoadingVisible.observe(viewLifecycleOwner) {
            changeLoadingVisibility(it)
        }
        viewModel.message.observe(viewLifecycleOwner) {
            showError(it)
        }
        viewModel.newsLiveData.observe(viewLifecycleOwner) {
            showNewsList(it)
        }
    }

    fun changeSource(source: Source) {
        this.source = source
        viewModel.loadNews(source)
    }

    private fun showNewsList(articles: List<Article?>?) {
        adapter.changeData(articles)
    }

    private fun showError(message: ViewMessage) {
        viewBinding.errorView.isVisible = true
        viewBinding.errorMessage.text = message.message
        viewBinding.tryAgain.text = message.posActionName
        viewBinding.tryAgain.setOnClickListener {
            message.posAction?.invoke()
        }
    }

    private fun changeLoadingVisibility(isLoadingVisible: Boolean) {
        viewBinding.progressBar.isVisible = isLoadingVisible
    }
}