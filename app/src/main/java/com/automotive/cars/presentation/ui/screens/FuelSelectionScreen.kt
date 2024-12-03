package com.automotive.cars.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.automotive.cars.R
import com.automotive.cars.data.local.model.FuelType
import com.automotive.cars.domain.model.Car
import com.automotive.cars.presentation.viewmodel.CarViewModel
import com.automotive.cars.presentation.state.UiState
import com.automotive.cars.presentation.ui.components.AppTopBar
import com.automotive.cars.presentation.ui.components.ItemRow
import com.automotive.cars.presentation.ui.components.SearchBar
import com.automotive.cars.presentation.ui.navigation.Routes


@Composable
fun FuelSelectionScreen(
    brandName: String,
    series: String,
    year: String,
    viewModel: CarViewModel,
    navController: NavHostController,
    query: String,
    onQueryChange: (String) -> Unit,
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.createCarState.collect { createCarState ->
            when (createCarState) {
                is UiState.Success -> {
                    navController.navigate(Routes.MY_CAR_PAGE) {
                        popUpTo(Routes.YEAR_SELECTION_PAGE) { inclusive = true }
                    }
                }
                is UiState.Error -> {
                    Toast.makeText(context, createCarState.message, Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
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
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            SearchBar(
                query = query,
                onQueryChange = onQueryChange,
                placeholderText = stringResource(R.string.txt_search_for_brand)
            )

            Text(
                text = "$brandName, $series, $year",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(top = 12.dp, start = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)
            ) {
                items(FuelType.all) { fuelType ->
                    ItemRow(
                        text = when (fuelType) {
                            is FuelType.Diesel -> "Diesel"
                            is FuelType.Gasoline -> "Gasoline"
                            is FuelType.Electric -> "Electric"
                            is FuelType.Hybrid -> "Hybrid"
                            is FuelType.Others -> "Others"
                            else -> "Unknown"
                        },
                        onClick = {
                            viewModel.createCar(
                                Car(
                                    brand = brandName,
                                    model = series,
                                    year = year,
                                    fuelType = fuelType ?: FuelType.Others,
                                )
                            )
                        }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

