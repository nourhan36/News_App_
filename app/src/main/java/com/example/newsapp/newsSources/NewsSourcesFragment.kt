package com.example.newsapp.newsSources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.api.model.sourcesResponse.SourcesResponse
import com.example.newsapp.databinding.FragmentNewsSourcesBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsSourcesFragment : Fragment() {
    lateinit var viewBinding: FragmentNewsSourcesBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentNewsSourcesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        getNewsSources()
    }

    private fun initViews() {
        viewBinding.tryAgain.setOnClickListener {
            viewBinding.errorView.isVisible = false
            getNewsSources()
        }
    }

    private fun getNewsSources() {
        changeLoadingVisibility(true)
        ApiManager
            .getServices()
            .getNewsSources()
            .enqueue(object : Callback<SourcesResponse> {
                override fun onFailure(
                    call: Call<SourcesResponse>,
                    t: Throwable
                ) {
                    changeLoadingVisibility(false)
                    showError(t.message)
                }


                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    changeLoadingVisibility(false)
                    if (response.isSuccessful) {
                        showNewsSources(response.body()?.sources)
                        return
                    }
                    val responseJson = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(
                        responseJson,
                        SourcesResponse::class.java
                    )
                    showError(errorResponse.message)
                }
            })
    }

    private fun showNewsSources(sources: List<Source?>?) {
        viewBinding.errorView.isVisible = false
        viewBinding.progressBar.isVisible = false
        sources?.forEach { source ->
            val tab = viewBinding.tabLayout.newTab()
            tab.text = source?.name
            viewBinding.tabLayout.addTab(tab)
        }
    }

    private fun showError(message: String?) {
        viewBinding.errorView.isVisible = true
        viewBinding.errorMessage.text = message
    }

    fun changeLoadingVisibility(isLoadingVisible: Boolean) {
        viewBinding.progressBar.isVisible = isLoadingVisible
    }
}