package com.example.newsapp.newsSources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.ViewMessage
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.databinding.FragmentNewsSourcesBinding
import com.example.newsapp.newsFragment.NewsFragment
import com.google.android.material.tabs.TabLayout

class NewsSourcesFragment(private var categories: String) : Fragment() {
    private lateinit var viewBinding: FragmentNewsSourcesBinding
    private lateinit var viewModel: SourcesViewModel
    val newsFragment = NewsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SourcesViewModel::class.java]
    }

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
        viewModel.getNewsSources(categories)
        initViews()
        observeLiveData()
    }

    private fun initViews() {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, newsFragment)
            .addToBackStack("")
            .commit()
    }

    private fun observeLiveData() {
        viewModel.isLoadingVisible.observe(viewLifecycleOwner) {
            changeLoadingVisibility(it)
        }
        viewModel.message.observe(viewLifecycleOwner) {
            showError(it)
        }
        viewModel.sourcesLiveData.observe(viewLifecycleOwner) {
            showNewsSources(it)
        }
    }

    private fun showNewsSources(sources: List<Source?>?) {

        viewBinding.errorView.isVisible = false
        viewBinding.progressBar.isVisible = false

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
