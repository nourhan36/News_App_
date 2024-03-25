package com.example.newsapp.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : Fragment() {

    private lateinit var viewBinding: FragmentSettingsBinding
    private lateinit var currentLanguage: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val languageItem = arrayListOf(getString(R.string.english), getString(R.string.arabic))

        val autoCompleteLanguage: AutoCompleteTextView = viewBinding.languageTv

        val adapterLanguage = ArrayAdapter(requireContext(), R.layout.list_item, languageItem)

        autoCompleteLanguage.setAdapter(adapterLanguage)

        currentLanguage = if (Locale.getDefault().language == "ar") {
            getString(R.string.arabic)
        } else {
            getString(R.string.english)
        }
        autoCompleteLanguage.setText(currentLanguage, false)

        autoCompleteLanguage.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedText = adapterView.getItemAtPosition(position)
            if (selectedText == getString(R.string.arabic)) {
                setLocale("ar")
            } else {
                setLocale("en")
            }
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        requireContext().resources.updateConfiguration(
            configuration,
            requireContext().resources.displayMetrics
        )

        requireActivity().recreate()
    }

}