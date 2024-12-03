package com.automotive.cars.domain.usecase

import com.automotive.cars.common.Resource
import com.automotive.cars.domain.model.Car
import com.automotive.cars.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllCarsUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    operator fun invoke(): Flow<Resource<List<Car>>> = flow {
        emit(Resource.Loading())
        try {
            carRepository.getAllCars().collect { carEntities ->
                val cars = carEntities.map { carEntity ->
                    Car(
                        id = carEntity.id,
                        brand = carEntity.brand,
                        model = carEntity.model,
                        year = carEntity.year,
                        fuelType = carEntity.fuelType,
                        isCurrentlySelected = carEntity.isCurrentlySelected,
                        supportedFeatures = carEntity.supportedFeatures
                    )
                }
                emit(Resource.Success(cars))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }
}