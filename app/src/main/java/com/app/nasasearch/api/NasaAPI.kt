package com.app.nasasearch.api

import com.app.nasasearch.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaAPI {

    @GET("/search")
    suspend fun searchQuery(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1
    ): Response<SearchResponse>
}