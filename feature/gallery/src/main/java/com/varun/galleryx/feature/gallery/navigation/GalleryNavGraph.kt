package com.varun.galleryx.feature.gallery.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.varun.galleryx.feature.gallery.ui.AlbumDetailScreen
import com.varun.galleryx.feature.gallery.ui.AlbumDetailViewModel
import com.varun.galleryx.feature.gallery.ui.AlbumScreen
import com.varun.galleryx.feature.gallery.ui.AlbumViewModel

@Composable
fun GalleryNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "gallery") {
        composable("gallery") {
            val viewModel: AlbumViewModel = hiltViewModel()
            AlbumScreen(navController, viewModel)
        }

        composable("albumDetail/{albumName}") { backStackEntry ->
            val albumName = backStackEntry.arguments?.getString("albumName") ?: return@composable
            val detailViewModel: AlbumDetailViewModel = hiltViewModel()
            AlbumDetailScreen(albumName, detailViewModel)
        }
    }
}