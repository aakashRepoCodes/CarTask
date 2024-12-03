package com.automotive.cars.domain.usecase

import com.automotive.cars.common.Resource
import com.automotive.cars.domain.model.Car
import com.automotive.cars.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDeleteCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    operator fun invoke(car: Car): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val rowsDeleted = carRepository.deleteCar(car)
            if (rowsDeleted > 0) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Failed to delete car"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }
}