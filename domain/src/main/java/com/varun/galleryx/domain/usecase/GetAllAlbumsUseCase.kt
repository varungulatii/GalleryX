package com.varun.galleryx.domain.usecase

import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.domain.repository.MediaRepository
import javax.inject.Inject

class GetAllAlbumsUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(): List<Album> = repository.getAllAlbums()
}