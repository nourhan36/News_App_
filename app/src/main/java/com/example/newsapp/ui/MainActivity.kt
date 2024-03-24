package com.example.newsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.newsSources.NewsSourcesFragment

class MainActivity : AppCompatActivity(), CategoriesFragment.CategoryClickListener {
    private lateinit var viewBinding: ActivityMainBinding
    private val newsSourcesFragment = NewsSourcesFragment()
    private val categoriesFragment = CategoriesFragment()
    private val settingsFragment = SettingsFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.imvMenu.setOnClickListener {
            viewBinding.root.openDrawer(GravityCompat.START)
        }

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

        viewBinding.root.closeDrawer(GravityCompat.START)
    }
}