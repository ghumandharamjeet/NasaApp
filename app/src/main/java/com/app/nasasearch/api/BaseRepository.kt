package com.app.nasasearch.api

import com.app.nasasearch.SearchResponse
import retrofit2.Response

interface BaseRepository {
    suspend fun searchQuery(searchQuery: String, pageNumber: Int) : Response<SearchResponse>
}