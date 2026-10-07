package com.pemmob.pipitanime.data.repository

import com.pemmob.pipitanime.data.model.Anime
import com.pemmob.pipitanime.data.model.Genre
import com.pemmob.pipitanime.data.remote.ApiService

class AnimeRepository(private val apiService: ApiService = ApiService.create()) {
    suspend fun searchAnime(query: String, genreId: Int? = null): Result<List<Anime>> {
        return runCatching {
            apiService.searchAnime(query, genreId?.toString()).data
        }
    }

    suspend fun getAnimeDetail(id: Int): Result<Anime> {
        return runCatching {
            apiService.getAnimeDetail(id).data
        }
    }

    suspend fun getGenres(): Result<List<Genre>> {
        return runCatching {
            apiService.getGenres().data
        }
    }
}