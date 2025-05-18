package com.varun.galleryx.feature.gallery

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.varun.galleryx.core.permission.PermissionHelper
import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.feature.gallery.navigation.GalleryUiState
import com.varun.galleryx.feature.gallery.navigation.GalleryViewModel

@Composable
fun GalleryScreen(navController: NavController, viewModel: GalleryViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (PermissionHelper.allPermissionsGranted(permissions)) {
            viewModel.loadAlbums()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(PermissionHelper.requiredPermissions())
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is GalleryUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is GalleryUiState.Success -> {
                    val albums = (uiState as GalleryUiState.Success).albums
                    LazyColumn{
                        items(albums) { album ->
                            AlbumListItem(album){
                                navController.navigate("albumDetail/${album.name}")
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

@Composable
fun AlbumListItem(album: Album, onClick: () -> Unit) {
    Text(
        text = "Album: ${album.name} - ${album.itemCount} items",
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    )
}