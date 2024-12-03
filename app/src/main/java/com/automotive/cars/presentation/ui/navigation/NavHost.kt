package com.automotive.cars.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            route = "${Routes.FUEL_SELECTION_PAGE}/{brandName}/{series}/{year}",
            arguments = listOf(
                navArgument("brandName") { type = NavType.StringType },
                navArgument("series") { type = NavType.StringType },
                navArgument("year") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val brandName = backStackEntry.arguments?.getString("brandName") ?: ""
            val series = backStackEntry.arguments?.getString("series") ?: ""
            val year = backStackEntry.arguments?.getString("year") ?: ""

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
                    navController.navigate("${Routes.MODEL_SELECTION_PAGE}/$brand")
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        // Model Selection Screen
        composable(
            route = "${Routes.MODEL_SELECTION_PAGE}/{brandName}",
            arguments = listOf(navArgument("brandName") { type = NavType.StringType })
        ) { backStackEntry ->
            val brandName = backStackEntry.arguments?.getString("brandName") ?: ""
            SeriesSelectionScreen(
                brandName = brandName,
                viewModel = dataProviderViewModel,
                onModelSelected = { brand, model ->
                    navController.navigate("${Routes.YEAR_SELECTION_PAGE}/$brand/$model")
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        // Year Selection Screen
        composable(
            route = "${Routes.YEAR_SELECTION_PAGE}/{brandName}/{modelName}",
            arguments = listOf(
                navArgument("brandName") { type = NavType.StringType },
                navArgument("modelName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val brandName = backStackEntry.arguments?.getString("brandName") ?: ""
            val modelName = backStackEntry.arguments?.getString("modelName") ?: ""
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

