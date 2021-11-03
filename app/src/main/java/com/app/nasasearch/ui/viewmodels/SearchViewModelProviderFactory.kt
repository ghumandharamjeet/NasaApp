package com.app.nasasearch.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.nasasearch.api.APIDelegate

class SearchViewModelProviderFactory(val app: Application, private val apiDelegate: APIDelegate): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(app, apiDelegate) as T
    }
}