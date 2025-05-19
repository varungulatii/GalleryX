package com.varun.galleryx.feature.gallery.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.feature.gallery.ui.components.AlbumCard

enum class LayoutMode {
    Grid, List
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreenFake(
    uiState: GalleryUiState,
    onAlbumClick: (Album) -> Unit
) {
    val layoutMode = rememberSaveable { mutableStateOf(LayoutMode.Grid) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GalleryX") },
                actions = {
                    IconButton(onClick = {
                        layoutMode.value =
                            if (layoutMode.value == LayoutMode.Grid) LayoutMode.List else LayoutMode.Grid
                    }) {
                        Icon(
                            imageVector = if (layoutMode.value == LayoutMode.Grid)
                                Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView,
                            contentDescription = "Toggle layout"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is GalleryUiState.Success -> {
                    val albums = uiState.albums
                    when (layoutMode.value) {
                        LayoutMode.Grid -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("albumGrid")
                            ) {
                                items(albums) { album ->
                                    AlbumCard(album = album) { onAlbumClick(album) }
                                }
                            }
                        }
                        LayoutMode.List -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("albumList")
                            ) {
                                items(albums) { album ->
                                    AlbumCard(album = album) { onAlbumClick(album) }
                                }
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}