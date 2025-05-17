package com.varun.galleryx.domain.usecase

import com.varun.galleryx.domain.model.MediaItem
import com.varun.galleryx.domain.repository.MediaRepository

class GetMediaInAlbumUseCase(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(albumName: String): List<MediaItem> = repository.getMediaInAlbum(albumName)
}