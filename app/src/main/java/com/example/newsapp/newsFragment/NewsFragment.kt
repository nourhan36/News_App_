package com.example.newsapp.newsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.newsResponse.Article
import com.example.newsapp.api.model.newsResponse.NewsResponse
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.databinding.FragmentNewsBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    private lateinit var viewBinding: FragmentNewsBinding
    private var source: Source? = null

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
    }

    fun changeSource(source: Source) {
        this.source = source
        loadNews()
    }

    private fun loadNews() {
        changeLoadingVisibility(true)
        source?.id?.let { sourceId ->
            ApiManager.getServices()
                .getNews(sources = sourceId)
                .enqueue(object : Callback<NewsResponse> {
                    override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                        changeLoadingVisibility(false)
                        showError(t.message)
                    }

                    override fun onResponse(
                        call: Call<NewsResponse>,
                        response: Response<NewsResponse>
                    ) {
                        changeLoadingVisibility(false)
                        if (response.isSuccessful) {
                            showNewsList(response.body()?.articles)
                        } else {
                            val responseJson = response.errorBody()?.string()
                            val errorResponse = Gson().fromJson(
                                responseJson,
                                NewsResponse::class.java
                            )
                            showError(errorResponse.message)
                        }
                    }
                })
        }
    }

    private val adapter = NewsAdapter(null)

    private fun showNewsList(articles: List<Article?>?) {
        adapter.changeData(articles)
    }

    private fun showError(message: String?) {
        viewBinding.errorView.isVisible = true
        viewBinding.errorMessage.text = message
    }

    private fun changeLoadingVisibility(isLoadingVisible: Boolean) {
        viewBinding.progressBar.isVisible = isLoadingVisible
    }

    private fun initViews() {
        viewBinding.newsRecycler.adapter = adapter
    }
}