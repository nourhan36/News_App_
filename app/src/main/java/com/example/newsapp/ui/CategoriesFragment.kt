package com.example.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsapp.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {
    lateinit var viewBinding: FragmentCategoriesBinding
    private lateinit var categoryClickListener: CategoryClickListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryClickListener = requireActivity() as CategoryClickListener

        viewBinding.imvSports.setOnClickListener {
            categoryClickListener.onCategoryClicked("Sports")
        }

    }

    var onCategoryClickedListener: CategoryClickListener? = null

    interface CategoryClickListener {
        fun onCategoryClicked(category: String)
    }

}