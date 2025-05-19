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
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE
        )
        val uri = MediaStore.Files.getContentUri("external")

        val selection = ("${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR " +
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?")

        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )

        val albumMap = mutableMapOf<String, MutableList<Pair<String, String>>>()
        val allImages = mutableListOf<String>()
        val allVideos = mutableListOf<String>()

        contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )?.use { cursor ->
            val bucketCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            val mimeTypeCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)

            while (cursor.moveToNext()) {
                val folderName = cursor.getString(bucketCol) ?: "Unknown"
                val filePath = cursor.getString(dataCol)
                val mimeType = cursor.getString(mimeTypeCol) ?: ""

                val fileType = if (mimeType.startsWith("image")) {
                    allImages.add(filePath)
                    "image"
                } else if (mimeType.startsWith("video")) {
                    allVideos.add(filePath)
                    "video"
                } else continue

                albumMap.getOrPut(folderName) { mutableListOf() }.add(filePath to fileType)
            }
        }

        val realAlbums = albumMap.map { (folderName, files) ->
            val imageThumb = files.firstOrNull { it.second == "image" }?.first
            val videoThumb = files.firstOrNull { it.second == "video" }?.first
            val thumbnail = imageThumb ?: videoThumb
            val isVideoThumb = imageThumb == null && videoThumb != null

            Album(
                name = folderName,
                itemCount = files.size,
                thumbnailUri = thumbnail.orEmpty(),
                isVideoThumbnail = isVideoThumb
            )
        }

        val virtualAlbums = mutableListOf<Album>()
        if (allImages.isNotEmpty()) {
            virtualAlbums.add(Album("All Images", allImages.size, allImages.first()))
        }
        if (allVideos.isNotEmpty()) {
            virtualAlbums.add(Album("All Videos", allVideos.size, allVideos.first()))
        }

        return (virtualAlbums + realAlbums).sortedByDescending { it.itemCount }
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
        val uri = MediaStore.Files.getContentUri("external")

        val selection: String
        val selectionArgs: Array<String>

        when (albumName) {
            "All Images" -> {
                selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
                selectionArgs = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
            }
            "All Videos" -> {
                selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
                selectionArgs = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
            }
            else -> {
                selection = "${MediaStore.MediaColumns.BUCKET_DISPLAY_NAME} = ?"
                selectionArgs = arrayOf(albumName)
            }
        }

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