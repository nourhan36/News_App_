package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.newsSources.NewsSourcesFragment

class MainActivity : AppCompatActivity(), CategoriesFragment.CategoryClickListener {
    private lateinit var viewBinding: ActivityMainBinding
    private val newsSourcesFragment = NewsSourcesFragment()
    private val categoriesFragment = CategoriesFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        categoriesFragment.categoryClickListener = this
        pushFragment(categoriesFragment)
        //pushFragment(SettingsFragment())
    }

    override fun onCategoryClicked(category: String) {
        newsSourcesFragment.changeCategory(category)
        pushFragment(newsSourcesFragment)
    }

    private fun pushFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}