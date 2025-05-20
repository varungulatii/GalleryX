package com.varun.galleryx.feature.gallery.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.varun.galleryx.feature.gallery.ui.AlbumDetailScreen
import com.varun.galleryx.feature.gallery.ui.AlbumScreen

@Composable
fun GalleryNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "album") {
        composable("album") {
            AlbumScreen(navController)
        }

        composable("albumDetail/{albumName}") { backStackEntry ->
            val albumName = backStackEntry.arguments?.getString("albumName") ?: return@composable
            AlbumDetailScreen(albumName, navController)
        }
    }
}