package com.example.moveeapp_compose_kmm.ui.scene.moviedetailscreen

import com.example.moveeapp_compose_kmm.data.uimodel.MovieDetailUiModel

data class MovieDetailUiState(
    val isLoading: Boolean = true,
    val movieDetailData: MovieDetailUiModel = MovieDetailUiModel(),
    val error: String? = null
)