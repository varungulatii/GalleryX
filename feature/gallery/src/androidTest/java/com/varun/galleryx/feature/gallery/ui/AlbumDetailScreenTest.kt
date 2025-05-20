package com.varun.galleryx.feature.gallery.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.rememberNavController
import com.varun.galleryx.domain.model.MediaItem
import com.varun.galleryx.domain.model.MediaType
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AlbumDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var viewModel: AlbumDetailViewModel

    @Before
    fun setup(){
        val savedStateHandle = SavedStateHandle(mapOf("albumName" to "Camera"))
        viewModel = AlbumDetailViewModel(
            getMediaInAlbumUseCase = mockk(relaxed = true),
            savedStateHandle = savedStateHandle
        )
    }

    @SuppressLint("ViewModelConstructorInComposable")
    @Test
    fun albumDetailScreen_showsNoMediaText_whenMediaListIsEmpty(){

        composeTestRule.setContent {
            AlbumDetailScreen(albumName = "Camera",
                navController = rememberNavController(),
                viewModel = viewModel,
                injectedUiState = AlbumDetailUiState.Success(emptyList())
            )
        }
        composeTestRule.onNodeWithText("No media found").assertExists()
    }

    @Test
    fun albumDetailScreen_displaysMediaItems() {
        val dummyMedia = listOf(
            MediaItem(id = 1, uri = Uri.parse("content://media/external/images/media/1"), mimeType = "image/jpeg", displayName = "Photo 1", folderName = "Camera", mediaType = MediaType.IMAGE),
            MediaItem(id = 2, uri = Uri.parse("content://media/external/images/media/1"), mimeType = "video/mp4", displayName = "Video 1", folderName = "Camera", mediaType = MediaType.VIDEO)
        )

        composeTestRule.setContent {
            AlbumDetailScreen(
                albumName = "Camera",
                navController = rememberNavController(),
                viewModel = viewModel,
                injectedUiState = AlbumDetailUiState.Success(dummyMedia)
            )
        }

        composeTestRule.onNodeWithContentDescription("Photo 1").assertExists()
    }
}

