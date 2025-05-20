package com.varun.galleryx.feature.gallery.ui

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.varun.galleryx.domain.model.Album
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class AlbumScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @SuppressLint("ViewModelConstructorInComposable")
    @Test
    fun albumCards_areDisplayed_whenUiStateIsSuccess() {
        val dummyAlbums = listOf(
            Album(name = "Camera", itemCount = 12, thumbnailUri = "file://img1.jpg"),
            Album(name = "Screenshots", itemCount = 5, thumbnailUri = "file://img2.jpg")
        )

        composeTestRule.setContent {
            AlbumScreen(
                navController = rememberNavController(),
                viewModel = DummyAlbumViewModel() ,
                injectedUiState = GalleryUiState.Success(dummyAlbums),
                permissionsOverride = true
            )
        }

        composeTestRule.onNodeWithTag("albumCard_Camera").assertExists() // Camera card should be visible
        composeTestRule.onNodeWithTag("albumCard_Screenshots").assertExists() // Screenshots card should be visible
    }

    @SuppressLint("ViewModelConstructorInComposable")
    @Test
    fun layoutToggles_betweenGridAndList() {
        val dummyAlbums = listOf(
            Album(name = "Camera", itemCount = 12, thumbnailUri = "file://img1.jpg"),
            Album(name = "Screenshots", itemCount = 5, thumbnailUri = "file://img2.jpg")
        )

        composeTestRule.setContent {
            AlbumScreen(
                navController = rememberNavController(),
                viewModel = DummyAlbumViewModel() ,
                injectedUiState = GalleryUiState.Success(dummyAlbums),
                permissionsOverride = true
            )
        }

        composeTestRule.onNodeWithTag("albumGrid").assertExists() // Grid should be visible
        composeTestRule.onNodeWithTag("albumList").assertDoesNotExist() // List should not be visible
        composeTestRule.onNodeWithContentDescription("Toggle layout").performClick() // Toggle to list
        composeTestRule.onNodeWithTag("albumList").assertExists() // List should be visible
        composeTestRule.onNodeWithTag("albumGrid").assertDoesNotExist() // Grid should not be visible
    }
}

class DummyAlbumViewModel : AlbumViewModel(
    getAllAlbumsUseCase = mockk(relaxed = true)
)
