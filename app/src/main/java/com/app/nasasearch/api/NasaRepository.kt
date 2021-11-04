package com.app.nasasearch.api

import com.app.newstoday.api.RetrofitInstance

class NasaRepository: BaseRepository {

    override suspend fun searchQuery(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchQuery(searchQuery, pageNumber)
}