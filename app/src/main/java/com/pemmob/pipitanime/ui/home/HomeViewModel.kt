package com.pemmob.pipitanime.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.pipitanime.data.model.Anime
import com.pemmob.pipitanime.data.model.Genre
import com.pemmob.pipitanime.data.repository.AnimeRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Semua state Home dikumpulkan di sini (state-driven UI)
data class HomeUiState(
    val query: String = "",
    val animeList: List<Anime> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val selectedGenreId: Int? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@OptIn(FlowPreview::class)
class HomeViewModel(
    private val repository: AnimeRepository = AnimeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadGenres()
        observeSearch()
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
    }

    fun onGenreSelected(genreId: Int?) {
        // klik genre yang sama = batalkan filter
        _uiState.update {
            it.copy(selectedGenreId = if (it.selectedGenreId == genreId) null else genreId)
        }
    }

    fun retry() {
        val s = _uiState.value
        viewModelScope.launch { search(s.query.trim(), s.selectedGenreId) }
    }

    // Query/genre berubah -> tunggu 500ms (debounce) -> panggil API
    private fun observeSearch() {
        viewModelScope.launch {
            _uiState
                .map { it.query.trim() to it.selectedGenreId }
                .distinctUntilChanged()
                .debounce(500)
                .collectLatest { (query, genreId) -> search(query, genreId) }
        }
    }

    private suspend fun search(query: String, genreId: Int?) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        repository.searchAnime(query, genreId)
            .onSuccess { list ->
                _uiState.update { it.copy(animeList = list, isLoading = false) }
            }
            .onFailure { e ->
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Terjadi kesalahan jaringan")
                }
            }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            repository.getGenres().onSuccess { genres ->
                _uiState.update { it.copy(genres = genres) }
            }
        }
    }
}
