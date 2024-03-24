package com.example.newsapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsapp.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {
    private lateinit var viewBinding: FragmentCategoriesBinding
    lateinit var categoryClickListener: CategoryClickListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.imvSports.setOnClickListener {
            categoryClickListener.onCategoryClicked("sports")
        }
        viewBinding.imvPolitics.setOnClickListener {
            categoryClickListener.onCategoryClicked("general")
        }
        viewBinding.imvHealth.setOnClickListener {
            categoryClickListener.onCategoryClicked("health")
        }
        viewBinding.imvBusiness.setOnClickListener {
            categoryClickListener.onCategoryClicked("business")
        }
        viewBinding.imvEnvironment.setOnClickListener {
            categoryClickListener.onCategoryClicked("general")
        }
        viewBinding.imvScience.setOnClickListener {
            categoryClickListener.onCategoryClicked("science")
        }

    }

    interface CategoryClickListener {
        fun onCategoryClicked(category: String)
    }

}