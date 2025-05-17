package com.varun.galleryx.domain.usecase

import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.domain.repository.MediaRepository

class GetAllAlbumsUseCase(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(): List<Album> = repository.getAllAlbums()
}