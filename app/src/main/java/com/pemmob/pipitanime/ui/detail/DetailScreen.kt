package com.pemmob.pipitanime.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pemmob.pipitanime.data.model.Anime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    malId: Int,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Ambil data detail sekali ketika screen dibuka atau ketika malId berbeda
    LaunchedEffect(malId) {
        if (state.anime?.malId != malId) {
            viewModel.loadDetail(malId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Anime", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.error != null -> Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Gagal memuat detail:\n${state.error}",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { viewModel.loadDetail(malId) }) { Text("Coba lagi") }
                }

                else -> state.anime?.let { DetailContent(it) }
            }
        }
    }
}

@Composable
private fun DetailContent(anime: Anime) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = anime.images?.jpg?.imageUrl,
            contentDescription = anime.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Text(anime.title ?: "-", style = MaterialTheme.typography.titleLarge)

        InfoRow("Genre", anime.genres?.joinToString(", ") { it.name }?.ifBlank { null } ?: "-")
        InfoRow("Rating", anime.score?.toString() ?: "N/A")
        InfoRow("Episode", anime.episodes?.toString() ?: "?")
        InfoRow("Status", anime.status ?: "-")

        Spacer(Modifier.height(8.dp))
        Text("Sinopsis", style = MaterialTheme.typography.titleMedium)
        Text(
            anime.synopsis ?: "Sinopsis tidak tersedia.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(80.dp)
        )
        Text(
            ": $value",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}
