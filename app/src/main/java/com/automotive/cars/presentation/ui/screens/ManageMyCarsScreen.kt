package com.automotive.cars.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.automotive.cars.R
import com.automotive.cars.domain.model.Car
import com.automotive.cars.presentation.viewmodel.CarViewModel
import com.automotive.cars.presentation.state.UiState
import com.automotive.cars.presentation.ui.components.AppTopBar
import com.automotive.cars.presentation.ui.navigation.Routes

@Composable
fun ManageMyCarsScreen(
    viewModel: CarViewModel,
    navController: NavController,
    onAddCarClick: () -> Unit,
    onBackPressed: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getMyCars()
    }
    val myCarsState by viewModel.getMyCarsState.collectAsState()

    val deleteCarState by viewModel.deleteCarState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(deleteCarState) {
        if (deleteCarState is UiState.Error) {
            val errorMessage = (deleteCarState as UiState.Error).message
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.txt_header_your_cars),
                onBackPressed = onBackPressed
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                when (myCarsState) {
                    is UiState.Success -> {
                        val cars = (myCarsState as UiState.Success<List<Car>>).data
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(cars) { car ->
                                CarItem(car,
                                    onCarSelected = {
                                        car.id?.let {
                                            viewModel.updateSelectedCar(it)
                                            navController.navigate(Routes.MY_CAR_PAGE)
                                        }
                                    },
                                    onDeleteClick = {
                                        car.let {
                                            viewModel.deleteCar(it)
                                            viewModel.getMyCars()
                                        }
                                    })
                            }
                        }
                    }

                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White
                        )
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            LocalContext.current, (myCarsState as UiState.Error).message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }

            GradientButton(
                text = stringResource(R.string.txt_add_new_car_btn),
                onClick = onAddCarClick,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 60.dp, end = 60.dp)
            .height(40.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        Color(0xFFFFD600)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
    }
}


@Composable
fun CarItem(
    car: Car,
    onCarSelected: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
            .let { baseModifier ->
                if (car.isCurrentlySelected) {
                    baseModifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(8.dp)
                    )
                } else {
                    baseModifier
                }
            }
            .clickable { onCarSelected.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Text(
                text = "${car.brand} - ${car.model}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "${car.year} - ${car.fuelType}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
        if (!car.isCurrentlySelected) {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Car",
                    tint = Color.White,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}
