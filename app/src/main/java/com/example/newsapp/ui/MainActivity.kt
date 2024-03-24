package com.example.newsapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.newsSources.NewsSourcesFragment
import com.example.newsapp.ui.categories.CategoriesFragment
import com.example.newsapp.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity(), CategoriesFragment.CategoryClickListener {
    private lateinit var viewBinding: ActivityMainBinding
    private val newsSourcesFragment = NewsSourcesFragment()
    private val categoriesFragment = CategoriesFragment()
    private val settingsFragment = SettingsFragment()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.imvMenu.setOnClickListener {
            viewBinding.root.openDrawer(GravityCompat.START)
        }

        changeSearchVisibility(false)

        viewBinding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.categoriesMenuItem -> {
                    val fragment = categoriesFragment
                    pushFragment(fragment)
                    true
                }

                R.id.settingsMenuItem -> {
                    val fragment = settingsFragment
                    pushFragment(fragment)
                    true
                }

                else -> {
                    val fragment = categoriesFragment
                    pushFragment(fragment)
                    true
                }
            }
        }

        categoriesFragment.categoryClickListener = this
        pushFragment(categoriesFragment)
        //pushFragment(SettingsFragment())

        viewBinding.imvSearch.setOnClickListener {
            changeSearchVisibility(true)
        }

        viewBinding.ivClose.setOnClickListener {
            changeSearchVisibility(false)
        }

        viewBinding.etSearch.setOnEditorActionListener { view, _, _ ->
            onSearchClickListener?.onSearchClick(view.text.toString())
            true
        }
    }

    override fun onCategoryClicked(category: String) {
        newsSourcesFragment.changeCategory(category)
        pushFragment(newsSourcesFragment)
    }

    private fun pushFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("")
            .commit()

        viewBinding.root.closeDrawer(GravityCompat.START)
    }

    private var onSearchClickListener: OnSearchClickListener? = null
    fun setOnSearchClickListener(listener: OnSearchClickListener) {
        onSearchClickListener = listener
    }

    fun interface OnSearchClickListener {
        fun onSearchClick(query: String)
    }

    private fun changeSearchVisibility(isSearchVisible: Boolean) {
        viewBinding.searchContainer.isVisible = isSearchVisible
        viewBinding.imvMenu.isVisible = !isSearchVisible
        viewBinding.title.isVisible = !isSearchVisible
        viewBinding.imvSearch.isVisible = !isSearchVisible
    }
}