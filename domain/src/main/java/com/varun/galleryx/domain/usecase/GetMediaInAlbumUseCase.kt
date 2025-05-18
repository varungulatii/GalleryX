package com.varun.galleryx.domain.usecase

import com.varun.galleryx.domain.model.MediaItem
import com.varun.galleryx.domain.repository.MediaRepository
import javax.inject.Inject

class GetMediaInAlbumUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(albumName: String): List<MediaItem> = repository.getMediaInAlbum(albumName)
}