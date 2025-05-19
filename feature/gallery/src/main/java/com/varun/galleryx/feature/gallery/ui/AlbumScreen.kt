package com.varun.galleryx.feature.gallery.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.varun.galleryx.core.permission.PermissionHelper
import com.varun.galleryx.feature.gallery.ui.components.AlbumCard
import com.varun.galleryx.feature.gallery.ui.utils.LayoutMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(navController: NavController, viewModel: AlbumViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()
    val layoutMode = rememberSaveable { mutableStateOf(LayoutMode.Grid) }
    val permissionsGranted = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (PermissionHelper.allPermissionsGranted(permissions)) {
            permissionsGranted.value = true
            viewModel.loadAlbums()
        }
        else{
            permissionsGranted.value = false
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(PermissionHelper.requiredPermissions())
    }

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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!permissionsGranted.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Storage permissions are required to view your albums.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                        Button(onClick = {
                            permissionLauncher.launch(PermissionHelper.requiredPermissions())
                        }) {
                            Text("Retry")
                        }
                    }
                }
            }
            else {
                when (uiState) {
                    is GalleryUiState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is GalleryUiState.Success -> {
                        val albums = (uiState as GalleryUiState.Success).albums
                        when (layoutMode.value) {
                            LayoutMode.Grid -> {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    contentPadding = PaddingValues(4.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(albums) { album ->
                                        AlbumCard(album = album, onClick = {
                                            navController.navigate("albumDetail/${album.name}")
                                        })
                                    }
                                }
                            }

                            LayoutMode.List -> {
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    items(albums) { album ->
                                        AlbumCard(album = album) {
                                            navController.navigate("albumDetail/${album.name}")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is GalleryUiState.Error -> {
                        Text(
                            text = "Error: ${(uiState as GalleryUiState.Error).message}"
                        )
                    }
                }
            }
        }
    }
}