package com.app.nasasearch

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val collection: Collection
)

data class Collection (
    val href: String,
    val items: List<Item>,
    val links: List<CollectionLink>,
    val metadata: Metadata,
    val version: String
)

data class Item (
    val data: List<Datum>,
    val href: String,
    val links: List<ItemLink>
)

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
)

data class ItemLink (
    val href: String,
    val rel: String,
    val render: String
)

data class CollectionLink (
    val href: String,
    val prompt: String,
    val rel: String
)

data class Metadata (
    @SerializedName("total_hits")
    val totalHits: Long
)