package com.automotive.cars.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.automotive.cars.R
import com.automotive.cars.presentation.state.UiState
import com.automotive.cars.presentation.viewmodel.DataProviderViewModel
import com.automotive.cars.presentation.ui.components.AppTopBar
import com.automotive.cars.presentation.ui.components.ItemRow
import com.automotive.cars.presentation.ui.components.SearchBar

@Preview
@Composable
fun BrandSelectionScreenPreview() {
    BrandSelectionScreen(
        dataProviderViewModel = hiltViewModel(),
        onVehicleSelected = {},
        onBackPressed = {}
    )
}

@Composable
fun BrandSelectionScreen(
    dataProviderViewModel: DataProviderViewModel,
    onVehicleSelected: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    val brandState by dataProviderViewModel.brandsState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        dataProviderViewModel.getAllBrands()
    }

    LaunchedEffect(query) {
        dataProviderViewModel.updateSearchQuery(query)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.txt_header_car_selection),
                onBackPressed = onBackPressed
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            SearchBar(
                query = query,
                onQueryChange = { newQuery -> query = newQuery },
                placeholderText = stringResource(R.string.txt_search_for_brand)
            )


            when (brandState) {
                is UiState.Success -> {
                    val brandsList = (brandState as UiState.Success<List<String>>).data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(brandsList) { brand ->
                            ItemRow(
                                text = brand,
                                onClick = { onVehicleSelected(brand) }
                            )
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                            )
                        }
                    }
                }

                is UiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                is UiState.Error -> {
                    Toast.makeText(
                        LocalContext.current, (brandState as UiState.Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }
        }
    }
}
