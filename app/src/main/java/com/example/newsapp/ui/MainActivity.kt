package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.newsFragment.NewsFragment
import com.example.newsapp.newsSources.NewsSourcesFragment

class MainActivity : AppCompatActivity(), CategoriesFragment.CategoryClickListener {
    lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                CategoriesFragment()
            )
            .commit()

    }

    override fun onCategoryClicked(category: String) {
        when (category) {
            "Sports" -> pushFragment(NewsSourcesFragment())

        }
    }

    private fun pushFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}