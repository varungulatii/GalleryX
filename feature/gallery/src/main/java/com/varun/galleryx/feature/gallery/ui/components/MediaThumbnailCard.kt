package com.varun.galleryx.feature.gallery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.varun.galleryx.domain.model.MediaItem
import com.varun.galleryx.feature.gallery.R

@Composable
fun MediaThumbnailCard(item: MediaItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .memoryCacheKey(item.id.toString())
                .diskCacheKey(item.id.toString())
                .data(item.uri)
                .crossfade(true)
                .placeholder(R.drawable.placeholder_album)
                .error(R.drawable.placeholder_album)
                .size(500)
                .build(),
            contentDescription = item.displayName,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}