package com.varun.galleryx.domain.repository

import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.domain.model.MediaItem

interface MediaRepository {

    fun getAllAlbums(): List<Album>
    fun getMediaInAlbum(albumName: String): List<MediaItem>
}