package com.pemmob.pipitanime.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pemmob.pipitanime.data.model.Anime
import com.pemmob.pipitanime.data.model.Genre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAnimeClick: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pipit Anime Finder") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = { viewModel.onQueryChange(it) },
                label = { Text("Cari Anime...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (uiState.genres.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(uiState.genres) { genre ->
                        FilterChip(
                            selected = uiState.selectedGenreId == genre.malId,
                            onClick = { viewModel.onGenreSelected(genre.malId) },
                            label = { Text(genre.name) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    uiState.error != null -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error: ${uiState.error}",
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.retry() }) {
                                Text("Coba Lagi")
                            }
                        }
                    }
                    uiState.animeList.isEmpty() -> {
                        Text(
                            text = "Tidak ada anime ditemukan.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(uiState.animeList) { anime ->
                                AnimeItem(anime = anime, onClick = { onAnimeClick(anime.malId) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimeItem(anime: Anime, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = anime.images?.jpg?.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = anime.title ?: "-", style = MaterialTheme.typography.titleMedium)
                Text(text = "Tipe: ${anime.type ?: "-"}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Rating: ${anime.score ?: "-"}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
