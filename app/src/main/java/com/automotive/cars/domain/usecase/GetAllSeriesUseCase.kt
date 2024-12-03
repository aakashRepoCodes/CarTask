package com.automotive.cars.domain.usecase

import com.automotive.cars.common.Resource
import com.automotive.cars.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllSeriesUseCase @Inject constructor(
    private val repository: CarRepository
) {
    operator fun invoke(brandName: String): Flow<Resource<List<String>>> = flow {
        try {
            emit(Resource.Loading())
            val series = repository.getAllSeries(brandName)
            emit(Resource.Success(series))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred while fetching series"))
        }
    }
}