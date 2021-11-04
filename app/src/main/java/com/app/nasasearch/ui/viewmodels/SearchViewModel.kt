package com.app.nasasearch.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.nasasearch.NasaApplication
import com.app.nasasearch.SearchResponse
import com.app.nasasearch.api.BaseRepository
import com.app.nasasearch.api.NasaRepository
import com.app.newstoday.api.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SearchViewModel(
    private var app: Application,
    private var nasaRepository: BaseRepository
) : ViewModel() {

    val searchedData: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()
    var searchPage = 1
    var searchResponse: SearchResponse? = null
    var newSearchQuery:String? = null
    var oldSearchQuery:String? = null

    fun searchQuery(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        newSearchQuery = searchQuery
        searchedData.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = nasaRepository.searchQuery(searchQuery, searchPage)
                searchedData.postValue(handleSearchResponse(response))
            } else {
                searchedData.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> searchedData.postValue(Resource.Error("Network Failure"))
                else -> searchedData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleSearchResponse(response: Response<SearchResponse>) : Resource<SearchResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if(searchResponse == null || newSearchQuery != oldSearchQuery) {
                    searchPage = 1
                    oldSearchQuery = newSearchQuery
                    searchResponse = resultResponse
                } else {
                    searchPage++
                    val oldItems = searchResponse?.collection?.items
                    val newItems = resultResponse.collection.items
                    oldItems?.addAll(newItems)
                }
                return Resource.Success(searchResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = app.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}