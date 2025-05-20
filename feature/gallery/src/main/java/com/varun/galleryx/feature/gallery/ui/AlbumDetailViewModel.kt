package com.varun.galleryx.feature.gallery.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varun.galleryx.domain.model.MediaItem
import com.varun.galleryx.domain.usecase.GetMediaInAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AlbumDetailViewModel @Inject constructor(
    private val getMediaInAlbumUseCase: GetMediaInAlbumUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    private val albumName: String = savedStateHandle["albumName"] ?: ""

    init {
        loadMedia()
    }

    private fun loadMedia() {
        viewModelScope.launch {
            try {
                val mediaItems = getMediaInAlbumUseCase(albumName)
                _uiState.value = AlbumDetailUiState.Success(mediaItems)
            } catch (e: Exception) {
                _uiState.value = AlbumDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class AlbumDetailUiState {
    object Loading : AlbumDetailUiState()
    data class Success(val media: List<MediaItem>) : AlbumDetailUiState()
    data class Error(val message: String) : AlbumDetailUiState()
}