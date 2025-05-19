package com.varun.galleryx.domain.usecase

import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.domain.repository.MediaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetAllAlbumsUseCaseTest {

    private val mediaRepository = mockk<MediaRepository>()
    private lateinit var useCase: GetAllAlbumsUseCase

    @Before
    fun setup() {
        useCase = GetAllAlbumsUseCase(mediaRepository)
    }

    @Test
    fun `returns album list from repository`() = runTest {
        val dummyAlbums = listOf(Album("Camera", 10, "file://a.jpg"))
        coEvery { mediaRepository.getAllAlbums() } returns dummyAlbums

        val result = useCase()

        assertEquals(dummyAlbums, result)
        coVerify(exactly = 1) { mediaRepository.getAllAlbums() }
    }
}