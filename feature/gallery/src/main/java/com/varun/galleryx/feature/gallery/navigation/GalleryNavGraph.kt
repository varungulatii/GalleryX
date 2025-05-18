package com.varun.galleryx.feature.gallery.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.varun.galleryx.feature.gallery.GalleryScreen

@Composable
fun GalleryNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "gallery") {
        composable("gallery") {
            val viewModel: GalleryViewModel = hiltViewModel()
            GalleryScreen(viewModel)
        }
    }
}