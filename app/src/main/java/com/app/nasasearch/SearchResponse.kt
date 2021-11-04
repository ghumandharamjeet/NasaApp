package com.app.nasasearch

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchResponse(
    val collection: SearchedCollection
)

data class SearchedCollection (
    val href: String,
    val items: MutableList<SearchedItems>,
    val links: List<CollectionLink>,
    val metadata: Metadata,
    val version: String
)

data class SearchedItems (
    val data: MutableList<Datum>,
    val href: String,
    val links: List<ItemLink>
): Serializable

data class Datum (
    val center: String,

    @SerializedName("date_created")
    val dateCreated: String,

    val description: String,
    val keywords: List<String>,

    @SerializedName("media_type")
    val mediaType: String,

    @SerializedName("nasa_id")
    val nasaID: String,

    val title: String
) : Serializable

data class ItemLink (
    val href: String,
    val rel: String,
    val render: String
) : Serializable

data class CollectionLink (
    val href: String,
    val prompt: String,
    val rel: String
)

data class Metadata (
    @SerializedName("total_hits")
    val totalHits: Int
)