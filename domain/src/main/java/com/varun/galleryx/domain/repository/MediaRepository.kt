package com.varun.galleryx.domain.repository

import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.domain.model.MediaItem

interface MediaRepository {

    suspend fun getAllAlbums(): List<Album>
    suspend fun getMediaInAlbum(albumName: String): List<MediaItem>
}