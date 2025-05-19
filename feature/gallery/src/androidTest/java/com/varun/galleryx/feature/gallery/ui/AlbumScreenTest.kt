package com.varun.galleryx.feature.gallery.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.varun.galleryx.domain.model.Album
import org.junit.Rule
import org.junit.Test

class AlbumScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun albumCards_areDisplayed_whenUiStateIsSuccess() {
        val dummyAlbums = listOf(
            Album(name = "Camera", itemCount = 12, thumbnailUri = "file://img1.jpg"),
            Album(name = "Screenshots", itemCount = 5, thumbnailUri = "file://img2.jpg")
        )

        composeTestRule.setContent {
            AlbumScreenFake(
                uiState = GalleryUiState.Success(dummyAlbums),
                onAlbumClick = {}
            )
        }

        composeTestRule.onNodeWithTag("albumCard_Camera").assertExists() // Camera card should be visible
        composeTestRule.onNodeWithTag("albumCard_Screenshots").assertExists() // Screenshots card should be visible
    }

    @Test
    fun layoutToggles_betweenGridAndList() {
        val dummyAlbums = listOf(
            Album(name = "Camera", itemCount = 12, thumbnailUri = "file://img1.jpg"),
            Album(name = "Screenshots", itemCount = 5, thumbnailUri = "file://img2.jpg")
        )

        composeTestRule.setContent {
            AlbumScreenFake(
                uiState = GalleryUiState.Success(dummyAlbums),
                onAlbumClick = {}
            )
        }

        composeTestRule.onNodeWithTag("albumGrid").assertExists() // Grid should be visible
        composeTestRule.onNodeWithTag("albumList").assertDoesNotExist() // List should not be visible
        composeTestRule.onNodeWithContentDescription("Toggle layout").performClick() // Toggle to list
        composeTestRule.onNodeWithTag("albumList").assertExists() // List should be visible
        composeTestRule.onNodeWithTag("albumGrid").assertDoesNotExist() // Grid should not be visible
    }
}

