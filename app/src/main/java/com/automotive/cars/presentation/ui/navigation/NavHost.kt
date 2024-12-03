package com.automotive.cars.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.automotive.cars.presentation.ui.navigation.Routes.ARG_BRAND_NAME
import com.automotive.cars.presentation.ui.navigation.Routes.ARG_MODEL_NAME
import com.automotive.cars.presentation.ui.navigation.Routes.ARG_SERIES
import com.automotive.cars.presentation.ui.navigation.Routes.ARG_YEAR
import com.automotive.cars.presentation.viewmodel.CarViewModel
import com.automotive.cars.presentation.viewmodel.DataProviderViewModel
import com.automotive.cars.presentation.ui.screens.BrandSelectionScreen
import com.automotive.cars.presentation.ui.screens.DefaultLandingScreen
import com.automotive.cars.presentation.ui.screens.FuelSelectionScreen
import com.automotive.cars.presentation.ui.screens.ManageMyCarsScreen
import com.automotive.cars.presentation.ui.screens.SeriesSelectionScreen
import com.automotive.cars.presentation.ui.screens.MyCarScreen
import com.automotive.cars.presentation.ui.screens.YearSelectionScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    carViewModel: CarViewModel = hiltViewModel(), // Shared ViewModel
    dataProviderViewModel: DataProviderViewModel = hiltViewModel() // Shared ViewModel
) {
    val hasCarState by carViewModel.hasCarsState.collectAsState()
    LaunchedEffect(hasCarState) {
        when (hasCarState) {
            true -> navController.navigate(Routes.MY_CAR_PAGE) { popUpTo(0) }
            else ->  navController.navigate(Routes.LANDING_PAGE) { popUpTo(0) }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LANDING_PAGE
    ) {
        composable(route = Routes.MY_CAR_PAGE) {
            MyCarScreen(
                viewModel = carViewModel,
                onManageCarClicked = {
                    navController.navigate(Routes.MANAGE_MY_CARS_PAGE)
                }
            )
        }

        composable(route = Routes.LANDING_PAGE) {
            DefaultLandingScreen {
                navController.navigate(Routes.BRAND_SELECTION_PAGE)
            }
        }

        composable(route = Routes.MANAGE_MY_CARS_PAGE) {
            ManageMyCarsScreen(
                viewModel = carViewModel,
                navController = navController,
                onAddCarClick = {
                    navController.navigate(Routes.BRAND_SELECTION_PAGE)
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.FUEL_SELECTION_PAGE,
            arguments = listOf(
                navArgument(ARG_BRAND_NAME) { type = NavType.StringType },
                navArgument(ARG_SERIES) { type = NavType.StringType },
                navArgument(ARG_YEAR) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val brandName = backStackEntry.arguments?.getString(ARG_BRAND_NAME) ?: ""
            val series = backStackEntry.arguments?.getString(ARG_SERIES) ?: ""
            val year = backStackEntry.arguments?.getString(ARG_YEAR) ?: ""

            FuelSelectionScreen(
                brandName = brandName,
                series = series,
                year = year,
                viewModel = carViewModel,
                navController = navController,
                query = "",
                onQueryChange = { },
                onBackPressed = {
                    navController.popBackStack()
                }
            )

        }

        // Brand Selection Screen
        composable(route = Routes.BRAND_SELECTION_PAGE) {
            BrandSelectionScreen(
                dataProviderViewModel = dataProviderViewModel,
                onVehicleSelected = { brand ->
                    navController.navigate("series_selection/$brand")
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        // Series Selection Screen
        composable(
            route = Routes.SERIES_SELECTION_PAGE,
            arguments = listOf(navArgument(ARG_BRAND_NAME) { type = NavType.StringType })
        ) { backStackEntry ->
            val brandName = backStackEntry.arguments?.getString(Routes.ARG_BRAND_NAME) ?: ""
            SeriesSelectionScreen(
                brandName = brandName,
                viewModel = dataProviderViewModel,
                onModelSelected = { brand, model ->
                    navController.navigate("year_selection/$brand/$model")
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        // Year Selection Screen
        composable(
            route = Routes.YEAR_SELECTION_PAGE,
            arguments = listOf(
                navArgument(ARG_BRAND_NAME) { type = NavType.StringType },
                navArgument(ARG_MODEL_NAME) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val brandName = backStackEntry.arguments?.getString(ARG_BRAND_NAME) ?: ""
            val modelName = backStackEntry.arguments?.getString(ARG_MODEL_NAME) ?: ""
            YearSelectionScreen(
                viewModel = dataProviderViewModel,
                navController = navController,
                brand = brandName,
                model = modelName,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}

