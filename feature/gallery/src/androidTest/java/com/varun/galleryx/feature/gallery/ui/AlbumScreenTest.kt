package com.varun.galleryx.feature.gallery.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
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

        // Assert both albums are visible
        composeTestRule.onNodeWithTag("albumCard_Camera").assertExists()
        composeTestRule.onNodeWithTag("albumCard_Screenshots").assertExists()
    }
}

