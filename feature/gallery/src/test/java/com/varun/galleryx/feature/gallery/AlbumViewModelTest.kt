package com.varun.galleryx.feature.gallery

import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.domain.usecase.GetAllAlbumsUseCase
import com.varun.galleryx.feature.gallery.ui.AlbumViewModel
import com.varun.galleryx.feature.gallery.ui.GalleryUiState
import com.varun.galleryx.feature.gallery.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getAllAlbumsUseCase = mockk<GetAllAlbumsUseCase>()
    private lateinit var viewModel: AlbumViewModel

    @Before
    fun setup() {
        viewModel = AlbumViewModel(getAllAlbumsUseCase)
    }

    @Test
    fun `initial uiState is Loading`() {
        assert(viewModel.uiState.value is GalleryUiState.Loading)
    }

    @Test
    fun `loadAlbums emits Success when use case returns albums`() = runTest {
        val dummyAlbums = listOf(Album("Camera", 10, "uri"))
        coEvery { getAllAlbumsUseCase() } returns dummyAlbums

        viewModel.loadAlbums()

        advanceUntilIdle()
        coVerify(exactly = 1) { getAllAlbumsUseCase() }


        assert(viewModel.uiState.value is GalleryUiState.Success)
        val result = viewModel.uiState.value as GalleryUiState.Success
        assertEquals(dummyAlbums, result.albums)
    }

    @Test
    fun `loadAlbums emits Error when use case throws`() = runTest {
        coEvery { getAllAlbumsUseCase() } throws RuntimeException("Failed")

        viewModel.loadAlbums()

        advanceUntilIdle()

        val result = viewModel.uiState.value
        assert(result is GalleryUiState.Error)
        assertEquals("Failed", (result as GalleryUiState.Error).message)
    }
}