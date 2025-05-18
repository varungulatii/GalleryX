package com.varun.galleryx.feature.gallery.navigation

import androidx.lifecycle.ViewModel
import com.varun.galleryx.domain.usecase.GetMediaInAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val getMediaInAlbumUseCase: GetMediaInAlbumUseCase
) : ViewModel() {

}