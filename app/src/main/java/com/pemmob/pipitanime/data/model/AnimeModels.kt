package com.pemmob.pipitanime.data.model

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    @SerializedName("data") val data: List<Anime>
)

data class AnimeDetailResponse(
    @SerializedName("data") val data: Anime
)

data class GenreResponse(
    @SerializedName("data") val data: List<Genre>
)

data class Anime(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("score") val score: Double?,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("images") val images: ImageWrapper?,
    @SerializedName("genres") val genres: List<Genre>?
)

data class ImageWrapper(
    @SerializedName("jpg") val jpg: ImageUrl?
)

data class ImageUrl(
    @SerializedName("image_url") val imageUrl: String?
)

data class Genre(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("name") val name: String
)