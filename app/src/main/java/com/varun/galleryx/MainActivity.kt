package com.varun.galleryx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.varun.galleryx.feature.gallery.navigation.GalleryNavGraph
import com.varun.galleryx.ui.theme.GalleryXTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GalleryXTheme {
                val navController = rememberNavController()
                GalleryNavGraph(navController = navController)
            }
        }
    }
}