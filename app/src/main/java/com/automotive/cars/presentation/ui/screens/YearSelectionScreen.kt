package com.automotive.cars.presentation.ui.screens

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.automotive.cars.R
import com.automotive.cars.presentation.state.UiState
import com.automotive.cars.presentation.viewmodel.DataProviderViewModel
import com.automotive.cars.presentation.ui.components.AppTopBar
import com.automotive.cars.presentation.ui.components.ItemRow
import com.automotive.cars.presentation.ui.components.SearchBar
import com.automotive.cars.presentation.ui.navigation.Routes

@Composable
fun YearSelectionScreen(
    viewModel: DataProviderViewModel,
    navController: NavHostController,
    brand: String,
    model: String,
    onBackPressed: () -> Unit
) {
    val yearsState = viewModel.yearsState.collectAsState()
    var query by remember { mutableStateOf("") }


    LaunchedEffect(brand, model) {
        viewModel.fetchSupportedYears(brand,model)
    }
    LaunchedEffect(query) {
        viewModel.updateSearchQuery(query)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.txt_car_selection),
                onBackPressed = onBackPressed
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = query,
                onQueryChange = { newQuery -> query = newQuery },
                placeholderText = stringResource(R.string.txt_search_for_years)
            )

            Text(
                text = "$brand, $model",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when (val state = yearsState.value) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                is UiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.data) { year ->
                            ItemRow(
                                text = year,
                                onClick = {
                                    navController.navigate("${Routes.FUEL_SELECTION_PAGE}/$brand/$model/$year")
                                },
                                modifier = Modifier.background(MaterialTheme.colorScheme.background)
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                                thickness = 1.dp
                            )
                        }
                    }
                }

                is UiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Red),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> Unit
            }
        }
    }
}

