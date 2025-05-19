package com.varun.galleryx.domain.model

data class Album (
    val name: String,
    val itemCount: Int,
    val thumbnailUri: String?,
    val isVideoThumbnail: Boolean = false
)