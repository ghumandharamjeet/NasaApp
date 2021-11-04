package com.app.nasasearch.ui.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.nasasearch.api.BaseRepository
import com.app.nasasearch.api.NasaRepository
import javax.inject.Inject

class SearchViewModelProviderFactory @Inject constructor(
    val app: Application, private val nasaRepository: BaseRepository
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(app, nasaRepository) as T
    }
}