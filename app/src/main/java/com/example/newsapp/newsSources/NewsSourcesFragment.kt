package com.example.newsapp.newsSources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.api.model.sourcesResponse.SourcesResponse
import com.example.newsapp.databinding.FragmentNewsSourcesBinding
import com.example.newsapp.newsFragment.NewsFragment
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsSourcesFragment : Fragment() {
    private lateinit var viewBinding: FragmentNewsSourcesBinding
    val newsFragment = NewsFragment()
    private var category: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentNewsSourcesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        getNewsSources()
    }

    private fun initViews() {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, newsFragment)
            .addToBackStack("")
            .commit()
        viewBinding.tryAgain.setOnClickListener {
            viewBinding.errorView.isVisible = false
            getNewsSources()
        }
    }

    fun changeCategory(category: String) {
        this.category = category
        getNewsSources()
    }

    private fun getNewsSources() {
        if (::viewBinding.isInitialized) {
            changeLoadingVisibility(true)
        } else {
            return
        }

        ApiManager
            .getServices()
            .getNewsSources(category = category ?: "")
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
                    } else {
                        val responseJson = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(
                            responseJson,
                            SourcesResponse::class.java
                        )
                        showError(errorResponse.message)
                    }
                }
            })
    }

    private fun showNewsSources(sources: List<Source?>?) {
        if (::viewBinding.isInitialized) {
            viewBinding.errorView.isVisible = false
            viewBinding.progressBar.isVisible = false
        } else {
            return
        }

        sources?.forEach { source ->
            val tab = viewBinding.tabLayout.newTab()
            tab.text = source?.name
            tab.tag = source // tag
            viewBinding.tabLayout.addTab(tab)
        }
        viewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val source = tab?.tag as Source // will return as object so need casting
                newsFragment.changeSource(source) // hrbot l view b object "tag 3obara 3n object"
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val source = tab?.tag as Source
                newsFragment.changeSource(source)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
        viewBinding.tabLayout.getTabAt(0)?.select()
    }

    private fun showError(message: String?) {
        if (::viewBinding.isInitialized) {
            viewBinding.errorView.isVisible = true
            viewBinding.errorMessage.text = message
        } else {
            return
        }
    }

    fun changeLoadingVisibility(isLoadingVisible: Boolean) {
        if (::viewBinding.isInitialized) {
            viewBinding.progressBar.isVisible = isLoadingVisible
        } else {
            return
        }
    }

}
