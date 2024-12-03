package com.automotive.cars.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.automotive.cars.common.Resource
import com.automotive.cars.domain.repository.CarRepository
import javax.inject.Inject

class GetSupportedYearsByBrandUseCase @Inject constructor(
    private val repository: CarRepository
) {
    operator fun invoke(brandName: String, seriesName: String): Flow<Resource<List<String>>> = flow {
        try {
            emit(Resource.Loading())
            val years = repository.getAllSupportedYears(brandName, seriesName)
            emit(Resource.Success(years))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred while fetching supported years"))
        }
    }
}
