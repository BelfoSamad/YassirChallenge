package com.samadtch.yassirchallenge.android.ui.screens.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.samadtch.yassirchallenge.android.ui.common.shimmerEffect

@Composable
fun MovieScreen(
    id: Int,
    viewModel: MovieViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    //------------------------------- Declarations
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()

    //------------------------------- Effects
    LaunchedEffect(true) {
        viewModel.initialize(id)//Fetch Movie by Id
    }

    //------------------------------- UI
    Column {
        //Back Button
        FilledTonalIconButton(
            modifier = Modifier.padding(16.dp),
            enabled = !uiState.isLoading,
            onClick = { onBackPressed() }
        ) {
            Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
        }

        //Movie
        if (uiState.isLoading) {
            Column(Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .width(256.dp)
                        .height(328.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(MaterialTheme.shapes.large)
                        .shimmerEffect()
                )
                Spacer(Modifier.padding(16.dp))
                Box(
                    Modifier
                        .width(256.dp)
                        .height(32.dp)
                        .padding(start = 16.dp)
                        .shimmerEffect()
                )
                Spacer(Modifier.padding(4.dp))
                Box(
                    Modifier
                        .width(128.dp)
                        .height(32.dp)
                        .padding(start = 16.dp)
                        .shimmerEffect()
                )
                Spacer(Modifier.padding(4.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(256.dp)
                        .padding(horizontal = 16.dp)
                        .shimmerEffect()
                )
            }
        }
        else if (uiState.errorCode != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    imageVector = Icons.Default.ErrorOutline,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = null
                )
                Spacer(Modifier.padding(4.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "An Error occurred",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }
        else Column(Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)) {
            //Image
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(MaterialTheme.shapes.large),
                model = "https://image.tmdb.org/t/p/w500/${uiState.movie!!.posterPath}",
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            //Content
            Spacer(Modifier.padding(16.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = uiState.movie!!.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = uiState.movie!!.releaseDate.split("-")[0],
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                text = uiState.movie!!.overview,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}