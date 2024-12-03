package com.automotive.cars.data.local.model

sealed class FuelType {
    data object Gasoline : FuelType()
    data object Diesel : FuelType()
    data object Electric : FuelType()
    data object Hybrid : FuelType()
    data object Others : FuelType()

    companion object {
        val all = listOf(Diesel, Electric, Gasoline, Hybrid, Others)
    }
}