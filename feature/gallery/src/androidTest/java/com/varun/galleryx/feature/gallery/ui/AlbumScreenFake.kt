package com.varun.galleryx.feature.gallery.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.feature.gallery.ui.components.AlbumCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreenFake(
    uiState: GalleryUiState,
    onAlbumClick: (Album) -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("GalleryX") }) }) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            when (uiState) {
                is GalleryUiState.Success -> {
                    val albums = uiState.albums
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(albums) { album ->
                            AlbumCard(album = album) { onAlbumClick(album) }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}