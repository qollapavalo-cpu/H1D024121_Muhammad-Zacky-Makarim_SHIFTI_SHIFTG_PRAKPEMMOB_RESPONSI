package com.pemmob.pipitanime.data.remote

import com.pemmob.pipitanime.data.model.AnimeDetailResponse
import com.pemmob.pipitanime.data.model.AnimeResponse
import com.pemmob.pipitanime.data.model.GenreResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("genres") genreId: String? = null
    ): AnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(
        @Path("id") id: Int
    ): AnimeDetailResponse

    @GET("genres/anime")
    suspend fun getGenres(): GenreResponse

    companion object {
        private const val BASE_URL = "https://api.tenrai.org/v1/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}