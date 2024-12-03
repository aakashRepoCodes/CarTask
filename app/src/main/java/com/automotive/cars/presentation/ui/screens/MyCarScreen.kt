package com.automotive.cars.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.automotive.cars.R
import com.automotive.cars.domain.model.Car
import com.automotive.cars.presentation.viewmodel.CarViewModel
import com.automotive.cars.presentation.state.UiState
import com.automotive.cars.presentation.ui.components.FeatureData
import com.automotive.cars.presentation.ui.components.FeatureList
import com.automotive.cars.presentation.ui.components.MyCarHeader

@Preview
@Composable
fun MyCarPreview() {
    MyCarScreen(hiltViewModel(), {})
}

@Composable
fun MyCarScreen(
    viewModel: CarViewModel,
    onManageCarClicked: () -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.getMySelectedCar()
    }
    val myCarsState by viewModel.getMySelectedCarsState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        when (myCarsState) {
            is UiState.Success -> {
                val selectedCar = (myCarsState as UiState.Success<Car>).data
                MyCarContent(
                    car = selectedCar,
                    onManageCarClicked = onManageCarClicked
                )
            }

            is UiState.Error -> {
                Toast.makeText(
                    LocalContext.current, (myCarsState as UiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            else -> {}
        }
    }
}

@Composable
fun MyCarContent(
    car: Car,
    onManageCarClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            MyCarHeader(
                logoRes = R.drawable.app_logo,
                carName = "${car.brand} - ${car.model}",
                carDetails = "${car.year} - ${car.fuelType}",
                carImageRes = R.drawable.car_logo,
                onManageCarClicked = onManageCarClicked
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.txt_discover_your_car),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            val apiFeatures = car.supportedFeatures.map { feature ->
                FeatureData(feature) { }
            }
            FeatureList(features = apiFeatures)
        }
    }
}

