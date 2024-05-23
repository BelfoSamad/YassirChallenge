package com.samadtch.yassirchallenge.android.ui.screens.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.paging.LoadState
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.cash.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.samadtch.yassirchallenge.android.R
import com.samadtch.yassirchallenge.android.ui.common.shimmerEffect
import com.samadtch.yassirchallenge.models.Movie
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.http.HttpStatusCode

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onShowSnackbar: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    goMovie: (Int) -> Unit
) {
    //------------------------------- Declarations
    val uiState = viewModel.uiState.collectAsLazyPagingItems()
    var refreshing by remember { mutableStateOf(false) } //Refresh State
    var errorOccurred by remember { mutableStateOf(false) }
    val state = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            // Only Refresh if there is an Error
            if(errorOccurred) {
                refreshing = true
                uiState.refresh()
                refreshing = false
            }
        }
    )

    //------------------------------- UI
    Box(Modifier.pullRefresh(state)) {
        //Content
        Column(Modifier.fillMaxSize()) {

            //Data
            LazyColumn {

                //Top Section
                item {
                    Row(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .align(Alignment.CenterVertically),
                            imageVector = Icons.Default.Movie, contentDescription = null
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = stringResource(id = R.string.app_name),
                            style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )
                        )
                    }
                }

                //List of Movies
                items(uiState.itemCount) { index ->
                    val movie = uiState[index]
                    MovieItem(movie!!, onMovieClicked = goMovie)
                    Spacer(Modifier.padding(8.dp))
                }

                //Pager States (Loading/Errors)
                uiState.apply {
                    //Initial Loading
                    when (uiState.loadState.refresh) {
                        is LoadState.Loading -> {
                            errorOccurred = false
                            item {
                                MovieShimmerEffect(); Spacer(Modifier.padding(8.dp))
                                MovieShimmerEffect(); Spacer(Modifier.padding(8.dp))
                                MovieShimmerEffect(); Spacer(Modifier.padding(8.dp))
                                MovieShimmerEffect(); Spacer(Modifier.padding(8.dp))
                            }
                        }
                        is LoadState.Error -> {
                            //State is called multiple times, handle error only the first time
                            if(!errorOccurred) handleError(
                                error = (uiState.loadState.refresh as LoadState.Error).error as Exception,
                                onShowSnackbar = onShowSnackbar
                            )
                            errorOccurred = true
                        }
                        is LoadState.NotLoading -> {}
                    }

                    //Loading more Items
                    when (uiState.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                //Loading New Items
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp),
                                        color = MaterialTheme.colorScheme.tertiary,
                                    )
                                }
                            }
                        }
                        is LoadState.Error -> {
                            //If Error happens while appending data only show Snackbar
                            handleError(
                                error = (uiState.loadState.refresh as LoadState.Error).error as Exception,
                                onShowSnackbar = onShowSnackbar
                            )
                        }
                        is LoadState.NotLoading -> {}
                    }
                }
            }

            /*
             * errorOccurred is triggered only when there is an error while fetching data initially
             * since there will be no results show the error message
             */
            if (errorOccurred) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null
                    )
                    Spacer(Modifier.padding(4.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "An Error occurred",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        //Refresh Indicator
        PullRefreshIndicator(refreshing = refreshing, state = state, Modifier.align(Alignment.TopCenter))
    }
}

fun handleError(error: Exception, onShowSnackbar: (String) -> Unit) {
    val errorRequests = "You have reached the maximum number of requests. Please try again later."
    val errorServer = "We encountered an issue while contacting the server. Please try again later."
    val errorKey = "The API Key is invalid. To continue using our services, please update your app."
    val errorNetwork = "Unable to establish a connection. Please verify your network connection and attempt again."

    when (error) {
        is UnresolvedAddressException -> onShowSnackbar(errorNetwork)
        is ClientRequestException -> {
            when (error.response.status) {
                HttpStatusCode.Unauthorized -> onShowSnackbar(errorKey)
                HttpStatusCode.TooManyRequests -> onShowSnackbar(errorRequests)
                else -> onShowSnackbar(errorServer)
            }
        }
        is ServerResponseException -> {
            when (error.response.status) {
                HttpStatusCode.InternalServerError -> onShowSnackbar(errorServer)
                HttpStatusCode.ServiceUnavailable -> onShowSnackbar(errorServer)
                else -> onShowSnackbar(errorServer)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onMovieClicked: (Int) -> Unit) {
    Row(Modifier
            .fillMaxWidth()
            .clickable { onMovieClicked(movie.id) }) {
        AsyncImage(
            modifier = Modifier
                .width(128.dp)
                .height(164.dp)
                .padding(start = 16.dp)
                .clip(MaterialTheme.shapes.large),
            model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Spacer(Modifier.padding(8.dp))
        Column(Modifier.padding(top = 16.dp, end = 16.dp)) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = movie.releaseDate.split("-")[0],//or parse string to Date and use Calendar to get year
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun MovieShimmerEffect() {
    Row(Modifier.fillMaxWidth()) {
        Box(Modifier
                .width(128.dp)
                .height(164.dp)
                .padding(start = 16.dp)
                .clip(MaterialTheme.shapes.large)
                .shimmerEffect())
        Spacer(Modifier.padding(8.dp))
        Column(Modifier.padding(top = 16.dp, end = 16.dp)) {
            Box(
                Modifier
                    .width(256.dp)
                    .height(32.dp)
                    .shimmerEffect()
            )
            Spacer(Modifier.padding(4.dp))
            Box(
                Modifier
                    .width(128.dp)
                    .height(32.dp)
                    .shimmerEffect()
            )
        }
    }
}