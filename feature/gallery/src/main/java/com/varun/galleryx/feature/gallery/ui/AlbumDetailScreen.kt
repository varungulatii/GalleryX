package com.varun.galleryx.feature.gallery.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.varun.galleryx.feature.gallery.ui.components.MediaThumbnailCard
import com.varun.galleryx.feature.gallery.ui.utils.LayoutMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumName: String,
    viewModel: AlbumDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val layoutMode = rememberSaveable { mutableStateOf(LayoutMode.Grid) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = albumName) },
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
                        when (layoutMode.value) {
                            LayoutMode.Grid -> {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    contentPadding = PaddingValues(4.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(media) { item ->
                                        MediaThumbnailCard(item)
                                    }
                                }
                            }

                            LayoutMode.List -> {
                                LazyColumn(
                                    contentPadding = PaddingValues(4.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(media) { item ->
                                        MediaThumbnailCard(item)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}