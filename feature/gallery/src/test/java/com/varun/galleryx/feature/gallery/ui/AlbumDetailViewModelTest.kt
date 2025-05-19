package com.varun.galleryx.feature.gallery.ui

import androidx.lifecycle.SavedStateHandle
import com.varun.galleryx.domain.model.MediaItem
import com.varun.galleryx.domain.model.MediaType
import com.varun.galleryx.domain.usecase.GetMediaInAlbumUseCase
import com.varun.galleryx.feature.gallery.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumDetailViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getMediaInAlbumUseCase = mockk<GetMediaInAlbumUseCase>()

    @Test
    fun `loadMedia emits Success when use case returns media items`() = runTest {
        val dummyAlbum = "Camera"
        val dummyMedia = listOf(MediaItem(1, uri = mockk(), "image/jpeg", "img.jpg", "Camera", MediaType.IMAGE))

        coEvery { getMediaInAlbumUseCase(dummyAlbum) } returns dummyMedia

        val savedStateHandle = SavedStateHandle(mapOf("albumName" to dummyAlbum))
        val viewModel = AlbumDetailViewModel(getMediaInAlbumUseCase, savedStateHandle)

        advanceUntilIdle()

        val result = viewModel.uiState.value
        assert(result is AlbumDetailUiState.Success)
        assertEquals(dummyMedia, (result as AlbumDetailUiState.Success).media)
    }

    @Test
    fun `loadMedia emits Error when use case throws exception`() = runTest {
        val dummyAlbum = "Camera"
        coEvery { getMediaInAlbumUseCase(dummyAlbum) } throws RuntimeException("Boom")

        val savedStateHandle = SavedStateHandle(mapOf("albumName" to dummyAlbum))
        val viewModel = AlbumDetailViewModel(getMediaInAlbumUseCase, savedStateHandle)

        advanceUntilIdle()

        val result = viewModel.uiState.value
        assert(result is AlbumDetailUiState.Error)
        assertEquals("Boom", (result as AlbumDetailUiState.Error).message)
    }
}