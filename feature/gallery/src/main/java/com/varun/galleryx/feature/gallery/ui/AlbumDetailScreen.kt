package com.varun.galleryx.feature.gallery.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.varun.galleryx.domain.model.MediaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumName: String,
    viewModel: AlbumDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = albumName) })
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            when (uiState) {
                is AlbumDetailUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is AlbumDetailUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${(uiState as AlbumDetailUiState.Error).message}")
                    }
                }

                is AlbumDetailUiState.Success -> {
                    val media = (uiState as AlbumDetailUiState.Success).media
                    if (media.isEmpty()) {
                        Text("No media found", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(4.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(media) { item ->
                                MediaThumbnail(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaThumbnail(item: MediaItem) {
    Image(
        painter = rememberAsyncImagePainter(item.uri),
        contentDescription = item.displayName,
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}