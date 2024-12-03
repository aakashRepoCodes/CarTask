package com.automotive.cars.domain.repository

import com.automotive.cars.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {

    suspend fun getAllCars(): Flow<List<Car>>

    suspend fun insertCar(car: Car): Long

    suspend fun deleteCar(car: Car): Int

    suspend fun getAllBrands(): List<String>

    suspend fun getAllSeries(brandName: String): List<String>

    suspend fun getAllSupportedYears(brandName: String, seriesName: String): List<String>

    suspend fun getSupportedFeatures(brandName: String, seriesName: String): List<String>

}