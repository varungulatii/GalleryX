package com.varun.galleryx.domain.usecase

import com.varun.galleryx.domain.model.MediaItem
import com.varun.galleryx.domain.model.MediaType
import com.varun.galleryx.domain.repository.MediaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetMediaInAlbumUseCaseTest {

    private val mediaRepository = mockk<MediaRepository>()
    private lateinit var useCase: GetMediaInAlbumUseCase

    @Before
    fun setup() {
        useCase = GetMediaInAlbumUseCase(mediaRepository)
    }

    @Test
    fun `returns media items from repository for album name`() = runTest {
        val dummyMedia = listOf(
            MediaItem(
                id = 1,
                uri = mockk(),
                mimeType = "image/jpeg",
                displayName = "img.jpg",
                folderName = "Camera",
                mediaType = MediaType.IMAGE
            )
        )
        coEvery { mediaRepository.getMediaInAlbum("Camera") } returns dummyMedia

        val result = useCase("Camera")

        assertEquals(dummyMedia, result)
        coVerify(exactly = 1) { mediaRepository.getMediaInAlbum("Camera") }
    }
}