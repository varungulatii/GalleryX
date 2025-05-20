package com.varun.galleryx.feature.gallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varun.galleryx.domain.model.Album
import com.varun.galleryx.domain.usecase.GetAllAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AlbumViewModel @Inject constructor(
    private val getAllAlbumsUseCase: GetAllAlbumsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    fun loadAlbums() {
        viewModelScope.launch {
            try {
                val albums = getAllAlbumsUseCase()
                _uiState.value = GalleryUiState.Success(albums)
            } catch (e: Exception) {
                _uiState.value = GalleryUiState.Error(e.message.orEmpty())
            }
        }
    }
}

sealed class GalleryUiState {
    object Loading : GalleryUiState()
    data class Success(val albums: List<Album>) : GalleryUiState()
    data class Error(val message: String) : GalleryUiState()
}