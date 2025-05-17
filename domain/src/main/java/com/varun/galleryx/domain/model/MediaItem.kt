package com.varun.galleryx.domain.model

import android.net.Uri

data class MediaItem(
    val id: Long,
    val uri: Uri,
    val mimeType: String,
    val displayName: String,
    val folderName: String,
    val mediaType: MediaType
)