package com.app.nasasearch.api

import com.app.newstoday.api.RetrofitInstance

class APIDelegate {

    suspend fun searchQuery(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchQuery(searchQuery, pageNumber)
}