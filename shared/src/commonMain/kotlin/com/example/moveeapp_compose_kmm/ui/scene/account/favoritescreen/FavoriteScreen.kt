package com.example.moveeapp_compose_kmm.ui.scene.account.favoritescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.moveeapp_compose_kmm.MR
import com.example.moveeapp_compose_kmm.core.viewModel
import com.example.moveeapp_compose_kmm.data.uimodel.account.favorite.FavoriteMovieUiModel
import com.example.moveeapp_compose_kmm.data.uimodel.account.favorite.FavoriteTvUiModel
import com.example.moveeapp_compose_kmm.ui.components.BackPressedItem
import com.example.moveeapp_compose_kmm.ui.components.CardImageItem
import com.example.moveeapp_compose_kmm.ui.components.DateItem
import com.example.moveeapp_compose_kmm.ui.components.RateItem
import com.example.moveeapp_compose_kmm.ui.components.TextItem
import com.example.moveeapp_compose_kmm.ui.scene.account.FavoriteMovieUiState
import com.example.moveeapp_compose_kmm.ui.scene.account.FavoriteTvUiState
import com.example.moveeapp_compose_kmm.ui.scene.account.MediaType
import com.example.moveeapp_compose_kmm.ui.scene.moviedetailscreen.MovieDetailScreen
import com.example.moveeapp_compose_kmm.ui.scene.tvdetailscreen.TvDetailScreen
import dev.icerock.moko.resources.compose.fontFamilyResource
import dev.icerock.moko.resources.compose.stringResource

class FavoriteScreen(private val mediaType: MediaType) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: FavoriteViewModel = viewModel()
        val favoriteMovieUiState by viewModel.favoriteMovieUiState.collectAsState()
        val favoriteTvUiState by viewModel.favoriteTvUiState.collectAsState()

        when (mediaType) {
            MediaType.MOVIE -> {
                viewModel.getPopularMovie()

                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            TextItem(
                                text = stringResource(MR.strings.fav_movie),
                                fontSize = 20.sp,
                                fontFamily = fontFamilyResource(MR.fonts.sfpro.bold),
                                textColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
                        navigationIcon = {
                            BackPressedItem { navigator.pop() }
                        }
                    )
                }) { contentPadding ->
                    Box(modifier = Modifier.padding(top = contentPadding.calculateTopPadding())) {
                        Spacer(
                            modifier = Modifier.height(190.dp).fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        FavoriteMovieContent(favoriteMovieUiState = favoriteMovieUiState) {
                            navigator.push(MovieDetailScreen(it))
                        }
                    }
                }
            }

            MediaType.TV -> {
                viewModel.getPopularTv()

                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            TextItem(
                                text = stringResource(MR.strings.fav_tv),
                                fontSize = 20.sp,
                                fontFamily = fontFamilyResource(MR.fonts.sfpro.bold),
                                textColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
                        navigationIcon = {
                            BackPressedItem { navigator.pop() }
                        }
                    )
                }) { contentPadding ->
                    Box(modifier = Modifier.padding(top = contentPadding.calculateTopPadding())) {
                        Spacer(
                            modifier = Modifier.height(190.dp).fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        FavoriteTvContent(favoriteTvUiState = favoriteTvUiState) {
                            navigator.push(TvDetailScreen(it))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteTvContent(
    favoriteTvUiState: FavoriteTvUiState,
    onTvDetailClick: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(favoriteTvUiState.favoriteTvData) {
            FavoriteTvRow(favoriteTv = it) { tvId ->
                onTvDetailClick(tvId)
            }
        }
    }
}

@Composable
fun FavoriteMovieContent(
    favoriteMovieUiState: FavoriteMovieUiState,
    onMovieDetailClick: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(favoriteMovieUiState.favoriteMovieData) {
            FavoriteMovieRow(favoriteMovie = it) { movieId ->
                onMovieDetailClick(movieId)
            }
        }
    }
}

@Composable
fun FavoriteMovieRow(
    favoriteMovie: FavoriteMovieUiModel,
    onDetailClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onDetailClick.invoke(favoriteMovie.movieId) },
        shape = MaterialTheme.shapes.small,
    ) {
        Row {
            CardImageItem(imagePath = favoriteMovie.posterPath)
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TextItem(
                    text = favoriteMovie.title,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DateItem(date = favoriteMovie.releaseDate)
                    RateItem(rate = favoriteMovie.voteAverage.toString())
                }
            }
        }
    }
}

@Composable
fun FavoriteTvRow(
    favoriteTv: FavoriteTvUiModel,
    onDetailClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onDetailClick.invoke(favoriteTv.tvId) },
        shape = MaterialTheme.shapes.small,
    ) {
        Row {
            CardImageItem(imagePath = favoriteTv.posterPath)
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TextItem(
                    text = favoriteTv.title,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RateItem(rate = favoriteTv.voteAverage.toString())
                }
            }
        }
    }
}