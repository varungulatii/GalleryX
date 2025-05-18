package com.varun.galleryx.data.repository

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.domain.model.MediaItem
import com.varun.galleryx.domain.model.MediaType
import com.varun.galleryx.domain.repository.MediaRepository
import javax.inject.Inject
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext

class MediaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MediaRepository {

    override suspend fun getAllAlbums(): List<Album> {

        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DATA
        )
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val albumMap = mutableMapOf<String, MutableList<String>>()

        contentResolver.query(uri,
            projection,
            null,
            null,
            null)?.use { cursor ->
                val bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

                while ( cursor.moveToNext() ) {
                    val folderName = cursor.getString(bucketColumn) ?: "Unknown"
                    val filePath = cursor.getString(dataColumn)
                    albumMap.getOrPut(folderName) { mutableListOf() }.add(filePath)
                }
        }

        return albumMap.map { (folderName, filePaths) ->
            Album(
                name  = folderName,
                itemCount = filePaths.size,
                thumbnailUri = filePaths.firstOrNull().orEmpty()
            )
        }.sortedByDescending { it.itemCount }
    }

    override suspend fun getMediaInAlbum(albumName: String): List<MediaItem> {
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
        )
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val selection = "${MediaStore.MediaColumns.BUCKET_DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(albumName)

        val mediaItems = mutableListOf<MediaItem>()

        contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
            val bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(displayNameColumn)
                val filePath = cursor.getString(dataColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val folder = cursor.getString(bucketColumn)

                val mediaType = when {
                    mimeType.startsWith("image") -> MediaType.IMAGE
                    mimeType.startsWith("video") -> MediaType.VIDEO
                    else -> continue
                }

                val contentUri = "file://$filePath".toUri()

                mediaItems.add(
                    MediaItem(
                        id = id,
                        uri = contentUri,
                        mimeType = mimeType,
                        displayName = name,
                        folderName = folder,
                        mediaType = mediaType
                    )
                )
            }
        }

        return mediaItems
        }
}