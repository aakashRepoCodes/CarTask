package com.automotive.cars.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.automotive.cars.R
import com.automotive.cars.presentation.state.UiState
import com.automotive.cars.presentation.viewmodel.DataProviderViewModel
import com.automotive.cars.presentation.ui.components.AppTopBar
import com.automotive.cars.presentation.ui.components.ItemRow
import com.automotive.cars.presentation.ui.components.SearchBar

@Preview
@Composable
fun ModelSelectionScreenPreview() {
    SeriesSelectionScreen(
        brandName = "BMW",
        viewModel = hiltViewModel(),
        onModelSelected = { _, _ -> },
        onBackPressed = {}
    )
}

@Composable
fun SeriesSelectionScreen(
    brandName: String,
    viewModel: DataProviderViewModel,
    onModelSelected: (String, String) -> Unit,
    onBackPressed: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    val seriesState = viewModel.seriesState.collectAsState()

    // Fetch series for the brand
    LaunchedEffect(brandName) {
        viewModel.getAllSeries(brandName)
    }

    // Observe query changes
    LaunchedEffect(query) {
        viewModel.updateSearchQuery(query)
    }


    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.txt_car_selection),
                onBackPressed = onBackPressed
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            SearchBar(
                query = query,
                onQueryChange = { newQuery -> query = newQuery },
                placeholderText = stringResource(R.string.txt_search_for_brand)
            )

            Text(
                text = brandName,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(top = 12.dp, start = 16.dp)
            )

            when (val state = seriesState.value) {
                is UiState.Success -> {
                    val modelList = state.data.orEmpty()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp)
                    ) {
                        items(modelList) { model ->
                            ItemRow(
                                text = model,
                                onClick = { onModelSelected(brandName, model) }
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                thickness = 1.dp
                            )
                        }
                    }
                }

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                is UiState.Error -> {
                    Toast.makeText(
                        LocalContext.current, state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }
        }
    }
}

