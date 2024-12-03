package com.automotive.cars.domain.usecase

import com.automotive.cars.common.Resource
import com.automotive.cars.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllBrandsUseCase @Inject constructor(
    private val repository: CarRepository
) {
    operator fun invoke(): Flow<Resource<List<String>>> = flow {
        try {
            emit(Resource.Loading())
            val brands = repository.getAllBrands()
            emit(Resource.Success(brands))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred while fetching brands"))
        }
    }
}