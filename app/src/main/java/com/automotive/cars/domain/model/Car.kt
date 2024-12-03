package com.automotive.cars.domain.model

import com.automotive.cars.data.local.model.FuelType


data class Car (
    val id: Int?= null,
    val brand: String,
    val model: String,
    val year: String,
    val fuelType: FuelType,
    val isCurrentlySelected: Boolean = false,
    val supportedFeatures: List<String> = emptyList()
){
    //Just a comment
}
