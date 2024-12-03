package com.automotive.cars.presentation.ui.navigation

object Routes {

    // Argument Keys
    const val ARG_BRAND_NAME = "brandName"
    const val ARG_MODEL_NAME = "modelName"
    const val ARG_SERIES = "series"
    const val ARG_YEAR = "year"

    //----- Selection Screens
    const val BRAND_SELECTION_PAGE = "brand_selection"
    const val SERIES_SELECTION_PAGE = "series_selection/{$ARG_BRAND_NAME}"
    const val YEAR_SELECTION_PAGE = "year_selection/{$ARG_BRAND_NAME}/{$ARG_MODEL_NAME}"
    const val FUEL_SELECTION_PAGE = "fuel_selection/{$ARG_BRAND_NAME}/{$ARG_SERIES}/{$ARG_YEAR}"

    //-----  Primary Screens
    const val LANDING_PAGE = "landing_page"
    const val MY_CAR_PAGE = "my_car"

    //----- Dashboard
    const val MANAGE_MY_CARS_PAGE = "manage_my_cars"
}
