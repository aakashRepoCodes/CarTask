package com.automotive.cars.domain.usecase

import com.automotive.cars.common.Resource
import com.automotive.cars.domain.model.Car
import com.automotive.cars.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCreateCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {

    operator fun invoke(car: Car): Flow<Resource<Long>> = flow {
        try {
            emit(Resource.Loading())
            val rowId = carRepository.insertCar(car)
            if (rowId > 0) {
                emit(Resource.Success(rowId))
            } else {
                emit(Resource.Error("Failed to insert car"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }
}